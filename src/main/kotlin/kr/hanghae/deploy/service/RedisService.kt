package kr.hanghae.deploy.service

import kr.hanghae.deploy.repository.RedisRepository
import org.springframework.stereotype.Service
import java.util.*


@Service
class RedisService(
    private val redisRepository: RedisRepository,
) {

    fun registerQueue(value: Any) { // return Boolean
       redisRepository.zAddIfAbsent(value, score = System.currentTimeMillis().toDouble())
    }

    fun getQueueSize(): Long? {
        return redisRepository.zSize()
    }

    fun <T : Any> popQueue(count: Long, type: Class<T>): Queue<T> {
        return LinkedList(redisRepository.zPopMin(count, type) ?: emptySet())
    }

    fun getQueueOrder(value: Any): Long? {
        return redisRepository.zRank(value)
    }

    fun addComplete(key: String) {
        redisRepository.hAdd(key)
    }

    fun getComplete(key: String): String? {
        return redisRepository.hGet(key)
    }

    fun removeComplete(key: String) {
        redisRepository.hSize(key)
    }
}
