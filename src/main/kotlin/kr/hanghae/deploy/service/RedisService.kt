package kr.hanghae.deploy.service

import kr.hanghae.deploy.repository.RedisRepository
import org.springframework.data.redis.core.ZSetOperations
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.*

enum class Key {
    WAITING,
    COMPLETE,
    LOCK
}

@Service
class RedisService(
    private val redisRepository: RedisRepository,
) {

    val expireTime: Duration = Duration.ofMinutes(1)

    fun addValue(key: String, value: String) {
        redisRepository.addValue(key, value)
    }

    fun setExpire(key: String, time: Duration) {
        redisRepository.setExpire(key, time)
    }

    fun getValue(key: String): String {
        return redisRepository.getValue(key)
    }

    fun removeValue(key: String) {
        redisRepository.removeValue(key)
    }

    fun addZSetIfAbsent(key: String, value: String) {
       redisRepository.addZSetIfAbsent(key, value, score = System.currentTimeMillis().toDouble())
    }

    fun getZSetSize(key: String): Long? {
        return redisRepository.getZSetSize(key)
    }

    fun <T : Any> popZSetMin(key: String, count: Long, type: Class<T>): Queue<T> {
        return LinkedList(redisRepository.popZSetMin(key, count, type) ?: emptySet())
    }

    fun getZSetRank(key: String, value: Any): Long? {
        return redisRepository.getZSetRank(key, value)
    }

    fun getZSetRangeByScore(key: String, min: Double, max: Double): Set<String> {
        return redisRepository.getZSetRangeByScore(key, min, max)
    }

    fun addHash(key: String, hashKey: String) {
        redisRepository.addHash(key, hashKey)
    }

    fun getHash(key: String, hashKey: String): String? {
        return redisRepository.getHash(key, hashKey)
    }

    fun deleteHash(key: String, hashKey: String) {
        redisRepository.deleteHash(key, hashKey)
    }

    fun getHashSize(key: String): Long {
        return redisRepository.getHashSize(key)
    }

    fun flushAll() {
        redisRepository.flushAll()
    }
}
