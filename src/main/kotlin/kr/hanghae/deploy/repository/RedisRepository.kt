package kr.hanghae.deploy.repository

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class RedisRepository(
    private val redisTemplate: StringRedisTemplate,
) {

    fun addValue(key: String, value: String) {
        return redisTemplate.opsForValue().set(key, value)
    }

    fun getValue(key: String): String {
        return redisTemplate.opsForValue().get(key).toString()
    }

    fun increaseValue(key: String) {
        redisTemplate.opsForValue().increment(key)
    }

    fun setExpire(key: String, timeout: Duration): Boolean? {
        return redisTemplate.expire(key, timeout)
    }

    fun removeValue(key: String) {
        redisTemplate.delete(key)
    }

    fun hasValue(key: String): Boolean {
        return redisTemplate.opsForValue().get(key) != null
    }

    fun addZSet(key: String, value: String, score: Double): Boolean? {
        return redisTemplate.opsForZSet().add(key, value, score)
    }

    fun addZSetIfAbsent(key: String, value: String, score: Double): Boolean? {
        return redisTemplate.opsForZSet().addIfAbsent(key, value, score)
    }

    fun <T : Any> popZSetMin(key: String, count: Long, type: Class<T>): Set<T>? {
        return redisTemplate.opsForZSet().popMin(key, count)
            ?.map { type.cast(it.value) }
            ?.toSet()
    }

    fun getZSetRank(key: String, value: Any): Long? {
        return redisTemplate.opsForZSet().rank(key, value)
    }

    fun getZSetSize(key: String): Long? {
        return redisTemplate.opsForZSet().size(key)
    }

    fun getZSetRangeByScore(key: String, min: Double, max: Double): Set<String> {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max) ?: emptySet()
    }

    fun deleteAllZSet(key: String, hashKeys: Array<String>) {
        redisTemplate.opsForZSet().remove(key, *hashKeys)
    }

    fun getZSet(key: String, member: String): Boolean = redisTemplate.opsForZSet().rank(key, member) != null

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
