package kr.hanghae.deploy.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository

@Repository
class RedisRepository(
    private val redisTemplate: RedisTemplate<String, Any>,
) {

    val zOpsKey = "queue"
    val hashKey = "complete"

    fun zAddIfAbsent(value: Any, score: Double): Boolean? {
        return redisTemplate.opsForZSet().addIfAbsent(zOpsKey, value, score)
    }

    fun <T : Any> zPopMin(count: Long, type: Class<T>): Set<T>? {
        return redisTemplate.opsForZSet().popMin(zOpsKey, count)
            ?.map { type.cast(it.value) }
            ?.toSet()
    }

    fun zRank(value: Any): Long? {
        return redisTemplate.opsForZSet().rank(zOpsKey, value)
    }

    fun zSize(): Long? {
        return redisTemplate.opsForZSet().size(zOpsKey)
    }

    fun hAdd(key: String) {
        redisTemplate.opsForHash<String, String>().put(hashKey, key, "complete")
    }

    fun hGet(key: String): String? {
        return redisTemplate.opsForHash<String, String>().get(hashKey, key)
    }

    fun hSize(key: String): Long? {
        return redisTemplate.opsForHash<String, String>().delete(hashKey, key)
    }
}
