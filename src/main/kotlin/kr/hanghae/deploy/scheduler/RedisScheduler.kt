package kr.hanghae.deploy.scheduler

import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.service.RedisService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class RedisScheduler(
    private val userReader: UserReader,
    private val redisService: RedisService,
) {
    @Scheduled(fixedDelay = 100)
    @Transactional
    fun popQueue() {
        val uuid = redisService.popQueue(count = 1, type = String::class.java).poll()
//        uuid?.let {
//            val user = userReader.getByUUID(uuid)
//            user.updateWaiting(0)
//        }
        uuid?.let {
            redisService.addComplete(uuid)
        }
    }
}
