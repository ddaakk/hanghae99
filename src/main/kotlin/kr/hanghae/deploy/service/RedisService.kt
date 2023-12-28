package kr.hanghae.deploy.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Service


@Service
class RedisService(
    private val redisTemplate: StringRedisTemplate,
) {

    fun addQueue(uuid: String) {
        val now = System.currentTimeMillis()
        redisTemplate.opsForZSet().add("queue", uuid, now.toDouble());
    }

    fun getOrder(uuid: String): Long {
        return redisTemplate.opsForZSet().rank("queue", uuid) ?: 1L
    }
}
