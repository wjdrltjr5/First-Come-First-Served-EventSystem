package org.example.api.repository;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository

public class AppliedUserRepository {

    private final RedisTemplate<String, String> template;

    public AppliedUserRepository(RedisTemplate<String, String> template, @Qualifier("redisTemplate") RedisTemplate redisTemplate) {
        this.template = template;

    }
    public Long add(Long userId){
        return template.opsForSet().add("applied_user", userId.toString());
    }
}
