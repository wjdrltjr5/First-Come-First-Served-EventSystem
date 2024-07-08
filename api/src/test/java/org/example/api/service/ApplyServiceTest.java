package org.example.api.service;

import org.assertj.core.api.Assertions;
import org.example.api.repository.CouponRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class ApplyServiceTest {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @AfterEach
    void redisClear(){
        redisTemplate.delete("coupon_count");
        redisTemplate.delete("applied_user");
    }

    @Test
    void 한번만_응모() {
        // given
        applyService.apply(1L);
        // when
        long count = couponRepository.count();
        // then
        Assertions.assertThat(count).isEqualTo(1);
    }

    @Test
    void 여러명_응모() throws InterruptedException {
        // given
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        // when
        for(int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try{
                    applyService.apply(userId);
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        long count = couponRepository.count();
        // then
        Assertions.assertThat(count).isEqualTo(100);
    }

    @Test
    @DisplayName("같은 사람이 쿠폰을 발급 신청을해도 하나만 발급")
    void appy() throws InterruptedException {
        // given
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);
        // when
        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try{
                    applyService.apply(1L);
                }finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        long count = couponRepository.count();
        // then
        Assertions.assertThat(count).isEqualTo(1);
    }
}