package org.example.api.Service;

import org.assertj.core.api.Assertions;
import org.example.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ApplyServiceTest {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

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
        int threadCount = 500;
        ExecutorService executorService = Executors.newFixedThreadPool(12);
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
        Assertions.assertThat(count).isEqualTo(30);
    }
}