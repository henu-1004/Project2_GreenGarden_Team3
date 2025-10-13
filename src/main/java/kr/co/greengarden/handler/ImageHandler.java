package kr.co.greengarden.handler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Component
public class ImageHandler {
    @Value("${app.upload-dir:./uploads}")
    private String uploadDir;

    private static final String UPLOAD_URL_PREFIX = "/uploads/";

    public String saveImage(MultipartFile image, String subdir) {
        if (image == null || image.isEmpty()) return null;

        String ext = StringUtils.getFilenameExtension(image.getOriginalFilename());
        String filename = UUID.randomUUID() + (ext != null ? "." + ext.toLowerCase() : "");

        try {
            Path root = Paths.get(uploadDir).toAbsolutePath().normalize();

            // ✅ subdir 정리: 선행 슬래시/백슬래시 제거 (절대경로/루트 이탈 방지)
            String cleanSubdir = (subdir == null) ? "" : subdir.replaceAll("^[/\\\\]+", "");
            Path dir = cleanSubdir.isBlank() ? root : root.resolve(cleanSubdir).normalize();

            Files.createDirectories(dir);

            Path target = dir.resolve(filename).normalize();

            // ✅ 업로드 루트 밖으로 나가는 경우 차단
            if (!target.startsWith(root)) {
                throw new SecurityException("Invalid upload target path");
            }

            image.transferTo(target);

            String prefix = cleanSubdir.isBlank() ? "" : "/" + cleanSubdir;
            // DB에는 /uploads/... 형태로 저장 (WebConfig가 이 URL을 실제 파일로 매핑)
            return "/uploads" + prefix + "/" + filename;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 공개 경로(/uploads/...) → 실제 파일 시스템 경로
     */
    public Path toFilePath(String publicPath) {
        if (publicPath == null || publicPath.isBlank()) return null;

        String decoded = URLDecoder.decode(publicPath, StandardCharsets.UTF_8);
        if (!decoded.startsWith(UPLOAD_URL_PREFIX)) {
            throw new IllegalArgumentException("Not an uploads URL: " + publicPath);
        }

        String relative = decoded.substring(UPLOAD_URL_PREFIX.length()); // ex) "product/uuid.jpg"
        Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path file = root.resolve(relative).normalize();

        if (!file.startsWith(root)) {
            throw new SecurityException("Path traversal detected");
        }
        return file;
    }

    /**
     * 파일 1개 즉시 삭제 (실패해도 예외 던지지 않고 false 반환)
     */
    public boolean deleteNowByPublicPath(String publicPath) {
        try {
            Path file = toFilePath(publicPath);
            if (file == null) return false;

            // 필요하면 썸네일/파생파일 함께 삭제 패턴을 여기서 추가
            return Files.deleteIfExists(file);
        } catch (Exception ignore) {
            return false;
        }
    }

    /**
     * 여러 파일 즉시 삭제
     */
    public void deleteNowByPublicPaths(Collection<String> publicPaths) {
        if (publicPaths == null) return;
        for (String p : publicPaths) {
            deleteNowByPublicPath(p);
        }
    }

    /**
     * 트랜잭션 커밋 후 삭제 예약
     */
    public void deleteAfterCommit(Collection<String> publicPaths) {
        if (publicPaths == null || publicPaths.isEmpty()) return;

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                deleteNowByPublicPaths(publicPaths);
            }
        });
    }

    /**
     * 트랜잭션 커밋 후 단일 파일 삭제 예약
     */
    public void deleteAfterCommit(String publicPath) {
        if (publicPath == null) return;
        deleteAfterCommit(List.of(publicPath));
    }

    /**
     * 이미지 교체(수정): 새 파일 저장 → 커밋 후 기존 파일 삭제.
     * - 새 파일이 없으면 기존 경로 유지
     * - 트랜잭션 롤백 시에는 방금 저장한 새 파일을 정리(고아 방지)
     *
     * @return 새 공개 경로(또는 새 파일이 없으면 기존 경로)
     */
    public String replaceImage(MultipartFile newImage, String oldPublicPath, String subdir) {
        if (newImage == null || newImage.isEmpty()) {
            return oldPublicPath; // 변경 없음
        }

        // 1) 새 파일 먼저 저장
        String newPublicPath = saveImage(newImage, subdir);

        // 2) 커밋되면 기존 파일 삭제
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                deleteNowByPublicPath(oldPublicPath);
            }

            @Override
            public void afterCompletion(int status) {
                // 3) 롤백이면 방금 저장한 새 파일을 정리 (DB에는 반영 안 됐으므로 고아 방지)
                if (status != STATUS_COMMITTED) {
                    deleteNowByPublicPath(newPublicPath);
                }
            }
        });

        return newPublicPath;
    }

    /**
     * 여러 이미지 교체(목록): 새 리스트 저장 → 커밋 후 기존 리스트 삭제, 롤백 시 새 리스트 삭제.
     *
     * @return 새 공개 경로 리스트
     */
    public List<String> replaceImages(List<MultipartFile> newImages, List<String> oldPublicPaths, String subdir) {
        if (newImages == null || newImages.isEmpty()) {
            return oldPublicPaths; // 변경 없음
        }

        // 새 파일들 저장
        List<String> newPaths = newImages.stream()
                .filter(f -> f != null && !f.isEmpty())
                .map(f -> saveImage(f, subdir))
                .toList();

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                deleteNowByPublicPaths(oldPublicPaths);
            }

            @Override
            public void afterCompletion(int status) {
                if (status != STATUS_COMMITTED) {
                    deleteNowByPublicPaths(newPaths);
                }
            }
        });

        return newPaths;
    }
}
