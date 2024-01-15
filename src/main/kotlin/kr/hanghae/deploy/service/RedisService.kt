package kr.hanghae.deploy.service

import kr.hanghae.deploy.repository.RedisRepository
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

    fun calculateEntranceTime(uuid: String): String {
        val counter = getValue("counter").toLong()
        increaseValue("counter")
        return (
            System.currentTimeMillis()
                + (counter / getValue("throughput").toLong())
                * getValue("cycleInterval").toLong()
            ).toString()
    }

    fun calculateRemainTime(): String {
        return (
            getValue("counter").toLong()
                / getValue("throughput").toLong()
                * getValue("cycleInterval").toLong() / 60000
            )
            .toString()
    }

    fun getCounter(): Int {
        return getValue("counter").toInt()
    }

    fun addValue(key: String, value: String) {
        redisRepository.addValue(key, value)
    }

    fun setExpire(key: String, time: Duration) {
        redisRepository.setExpire(key, time)
    }

    fun getValue(key: String): String {
        return redisRepository.getValue(key)
    }

    fun increaseValue(key: String) {
        redisRepository.increaseValue(key)
    }

    fun hasValue(key: String): Boolean {
        return redisRepository.hasValue(key)
    }

    fun removeValue(key: String) {
        redisRepository.removeValue(key)
    }

    fun addZSet(key: String, value: String): Boolean? {
        return redisRepository.addZSet(
            key,
            value,
            score = System.currentTimeMillis().toDouble()
        )
    }

    fun addZSetIfAbsent(key: String, value: String) {
        redisRepository.addZSetIfAbsent(
            key,
            value,
            score = System.currentTimeMillis().toDouble()
        )
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

    fun getZSet(key: String, member: String): Boolean {
        return redisRepository.getZSet(key, member)
    }

    fun deleteAllZSet(key: String, hashKeys: Array<String>) {
        redisRepository.deleteAllZSet(key, hashKeys)
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
