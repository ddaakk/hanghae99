package kr.hanghae.deploy.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class RedisRepository(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    fun addValue(key: String, value: String) {
        return redisTemplate.opsForValue().set(key, value)
    }

    fun getValue(key: String): String {
        return redisTemplate.opsForValue().get(key).toString()
    }

    fun setExpire(key: String, timeout: Duration): Boolean? {
        return redisTemplate.expire(key, timeout)
    }

    fun removeValue(key: String) {
        redisTemplate.delete(key)
    }

    fun addZSetIfAbsent(zOpsKey: String, value: String, score: Double): Boolean? {
        return redisTemplate.opsForZSet().addIfAbsent(zOpsKey, value, score)
    }

    fun <T : Any> popZSetMin(zOpsKey: String, count: Long, type: Class<T>): Set<T>? {
        return redisTemplate.opsForZSet().popMin(zOpsKey, count)
            ?.map { type.cast(it.value) }
            ?.toSet()
    }

    fun getZSetRank(zOpsKey: String, value: Any): Long? {
        return redisTemplate.opsForZSet().rank(zOpsKey, value)
    }

    fun getZSetSize(zOpsKey: String): Long? {
        return redisTemplate.opsForZSet().size(zOpsKey)
    }

    fun getZSetRangeByScore(key: String, min: Double, max: Double): Set<String> {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max) ?: emptySet()
    }

    fun addHash(key: String, hashKey: String) {
        redisTemplate.opsForHash<String, String>().put(key, hashKey, "complete")
    }

    fun getHash(key: String, hashKey: String): String? {
        return redisTemplate.opsForHash<String, String>().get(key, hashKey)
    }

    fun deleteHash(key: String, hashKey: String): Long? {
        return redisTemplate.opsForHash<String, String>().delete(key, hashKey)
    }

    fun getHashSize(key: String): Long {
        return redisTemplate.opsForHash<String, String>().size(key)
    }

    fun flushAll() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushDb()
    }
}
