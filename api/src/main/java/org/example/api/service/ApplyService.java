package org.example.api.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.api.domain.Coupon;
import org.example.api.repository.CouponCountRepository;
import org.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApplyService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
    }
    @Transactional
    public void  apply(Long userId) {
        Long count = couponCountRepository.increment();
        if(count > 100){
            log.info("초과되었습니다.");
            return;
        }
        log.info("발급되었습니다.");
        couponRepository.save(new Coupon(userId));

    }
}
