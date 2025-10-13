package kr.co.greengarden.service;

import kr.co.greengarden.entity.Grade;
import kr.co.greengarden.entity.Point;
import kr.co.greengarden.repository.CartRepository;
import kr.co.greengarden.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
 * 날짜 : 2025/09/25
 * 이름 : 한탁원
 * 내용 : GradeService 작성
 */
@RequiredArgsConstructor
@Service
public class GradeService {
    
    private final GradeRepository gradeRepository;

    public Optional<Grade> getGrade(String memId){
        return gradeRepository.findById(memId);
    }


}
