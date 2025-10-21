package kr.co.greengarden.repository;

import java.time.LocalDateTime;

public interface IssuedRow {

    String getIssueId();
    String getCouponNo();
    String getType();
    String getName();
    String getUserId();
    LocalDateTime getUsedAt();
    String getIssueStatus();
    String getViewStatus(); // 미사용/사용/중단/만료
    Integer getCanStop(); // 1 or 0
}
