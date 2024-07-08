package org.example.api.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.api.domain.Coupon;
import org.example.api.repository.AppliedUserRepository;
import org.example.api.repository.CouponCountRepository;
import org.example.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ApplyService {

    private final CouponRepository couponRepository;
    private final CouponCountRepository couponCountRepository;
    private final AppliedUserRepository appliedUserRepository;

    public ApplyService(CouponRepository couponRepository, CouponCountRepository couponCountRepository, AppliedUserRepository appliedUserRepository) {
        this.couponRepository = couponRepository;
        this.couponCountRepository = couponCountRepository;
        this.appliedUserRepository = appliedUserRepository;
    }

    @Transactional
    public void  apply(Long userId) {
        Long apply = appliedUserRepository.add(userId);
        if(apply != 1) return;

        Long count = couponCountRepository.increment();
        if(count > 100){
            log.info("초과되었습니다.");
            return;
        }
        log.info("발급되었습니다.");
        couponRepository.save(new Coupon(userId));
    }
}
