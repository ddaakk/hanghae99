package kr.hanghae.deploy.service

import kr.hanghae.deploy.domain.emitter.EmitterRepository
import kr.hanghae.deploy.dto.message.MessageDto
import kr.hanghae.deploy.dto.message.response.WaitingMessageResponse
import mu.KotlinLogging
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException

@Service
class MessageService(
    private val rabbitTemplate: RabbitTemplate,
    private val emitterRepository: EmitterRepository,
    @Value("\${rabbitmq.exchange.name}") private val exchangeName: String,
    @Value("\${rabbitmq.routing.key}") private val routingKey: String,
    @Value("\${rabbitmq.queue.name}") private val queueName: String,
) {

    private val logger = KotlinLogging.logger {}
    var count: Int = 0

    /**
     * Queue로 메시지를 발행
     *
     * @param messageDto 발행할 메시지의 DTO 객체
     */
    fun sendMessage(messageDto: MessageDto) {
//        logger.info { "message sent: $messageDto" }
        rabbitTemplate.convertAndSend(exchangeName, routingKey, messageDto)
    }

    /**
     * Queue에서 메시지를 구독
     *
     * @param messageDto 구독한 메시지를 담고 있는 MessageDto 객체
     */
    @RabbitListener(queues = ["\${rabbitmq.queue.name}"])
    fun receiveMessage(messageDto: MessageDto) {

        val uuid = messageDto.content
        val eventId: String = uuid + "_" + System.currentTimeMillis()

        val rabbitAdmin = RabbitAdmin(rabbitTemplate.connectionFactory)
        val messageCount = rabbitAdmin.getQueueInfo(queueName).messageCount
        logger.info { "finish messaging uuid=$uuid, count=${++count}, messageCount=$messageCount" }

        emitterRepository.saveFinish(uuid)
//        emitterRepository.saveEventCache(eventId, waitingMessageResponse)

        val emitter = emitterRepository.findAllEmitterWithoutSelf(uuid)
        emitter.forEach { (id, emitter) ->
            run {
                try {
                    // 등록할 때 당시의 몇번째 인지 가져옴
                    // 근데 내가 현재 몇번째인지 가져오려면
                    val waitingMessageResponse = WaitingMessageResponse.from(emitter.waitingOrder)
                    emitter.emitter.send(
                        SseEmitter.event()
                            .id(eventId)
                            .name("sse")
                            .data(waitingMessageResponse)
                    )
                } catch (e: IOException) {
                    logger.error { e.message }
                    emitterRepository.deleteById(id)
                }
            }
        }

        emitterRepository.deleteById(uuid)
    }

//    fun getQueueWaitingCount(): Int {
//        return rabbitAdmin.getQueueInfo(queueName).messageCount
//    }
}
