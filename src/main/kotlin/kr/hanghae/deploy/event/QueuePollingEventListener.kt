package kr.hanghae.deploy.event

import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.service.Key
import kr.hanghae.deploy.service.RedisService
import mu.KotlinLogging
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class QueuePollingEventListener(
    private val redisService: RedisService,
) {

    @EventListener
    @Async
    fun pollQueue(queuePollingEvent: QueuePollingEvent) {
        val uuid = redisService.popZSetMin(count = 1, key = Key.WAITING.toString(), type = String::class.java).poll()
        redisService.addHash(Key.COMPLETE.toString(), "complete$uuid")

        logger.info { "유저가 " }
    }
}
