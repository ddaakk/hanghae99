package kr.hanghae.deploy.service

import kr.hanghae.deploy.domain.emitter.EmitterRepository
import kr.hanghae.deploy.domain.user.User
import kr.hanghae.deploy.domain.user.UserRepositoryImpl
import kr.hanghae.deploy.dto.message.MessageDto
import kr.hanghae.deploy.dto.user.request.ChargeBalanceRequest
import kr.hanghae.deploy.dto.user.response.ChargeBalanceResponse
import kr.hanghae.deploy.dto.user.response.GenerateTokenResponse
import kr.hanghae.deploy.dto.user.response.GetBalanceResponse
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException
import java.util.*


@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepositoryImpl,
    private val emitterRepository: EmitterRepository,
    private val messageService: MessageService,
) {

    val logger = KotlinLogging.logger {}
    var count: Int = 0

    @Transactional
    fun generateToken(lastEventId: String): SseEmitter {

        val uuid = UUID.randomUUID().toString()
        userRepository.save(User(uuid))
        logger.info { "connect with uuid= $uuid, count= ${++count}" }

//        messageService.sendMessage(MessageDto("uuid", uuid, messageService.getQueueWaitingCount()))
        messageService.sendMessage(MessageDto("uuid", uuid, 0))

        val emitterId = uuid + "_" + System.currentTimeMillis()
//        val emitter = emitterRepository.save(emitterId, SseEmitter(60L * 1000 * 60), messageService.getQueueWaitingCount())
        val emitter = emitterRepository.save(emitterId, SseEmitter(60L * 1000 * 60), 0)
//        emitter.onCompletion { emitterRepository.deleteById(emitterId) }
        emitter.emitter.onTimeout { emitterRepository.deleteById(emitterId) }

        sendConnectMessage(
            emitter.emitter, emitterId, emitterId,
            GenerateTokenResponse.from(uuid, 0)
        )

        // TODO("재접속 시 복구되도록 구현")
//        if (hasLostData(lastEventId)) {
//            sendLostData(lastEventId, uuid, emitterId, emitter);
//        }

        return emitter.emitter
    }

    fun verifyUser(token: String): Boolean {
        return userRepository.findByUuid(token) != null
    }

    private fun sendConnectMessage(emitter: SseEmitter, eventId: String, emitterId: String, data: Any) {
        try {
            emitter.send(
                SseEmitter.event()
                    .id(eventId)
                    .name("sse")
                    .data(data)
            )
        } catch (exception: IOException) {
            logger.error { "connect message sending error" }
            emitterRepository.deleteById(emitterId)
        }
    }

    private fun sendLostData(lastEventId: String, uuid: String, emitterId: String, emitter: SseEmitter) { // (6)
        val eventCaches: Map<String, Any> = emitterRepository.findAllEventCacheWithUuid(uuid)
        eventCaches.entries.stream()
            .filter { (key): Map.Entry<String, Any> ->
                lastEventId.compareTo(
                    key
                ) < 0
            }
            .forEach { (key, value): Map.Entry<String, Any> ->
                sendConnectMessage(
                    emitter,
                    key,
                    emitterId,
                    value
                )
            }
    }

    private fun hasLostData(lastEventId: String): Boolean {
        return lastEventId.isNotEmpty()
    }

    @Transactional
    fun deleteUser(uuid: String) {
        userRepository.deleteByUuid(uuid)
    }

    fun findUser(uuid: String): User {
        return userRepository.findByUuid(uuid) ?:
            throw IllegalArgumentException("사용자를 찾을 수 없습니다.")
    }

    @Transactional
    fun chargeBalance(
        chargeBalanceRequest: ChargeBalanceRequest,
        uuid: String
    ): ChargeBalanceResponse {
        val findUser = findUser(uuid)
        findUser.chargeBalance(chargeBalanceRequest.balance)
        return ChargeBalanceResponse.of(findUser)
    }

    fun getBalance(uuid: String): GetBalanceResponse {
        val findUser = findUser(uuid)
        return GetBalanceResponse.of(findUser)
    }
}
