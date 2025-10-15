package kr.co.greengarden.mapper;

import kr.co.greengarden.repository.CouponRepository;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CouponMapper {

    public List<CouponRepository> selectCouponList();
}
