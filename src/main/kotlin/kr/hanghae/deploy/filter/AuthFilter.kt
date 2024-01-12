package kr.hanghae.deploy.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.event.QueuePollingEvent
import kr.hanghae.deploy.service.Key
import kr.hanghae.deploy.service.RedisService
import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import java.time.Duration

private val logger = KotlinLogging.logger {}

@Component
class AuthFilter(
    private val objectMapper: ObjectMapper,
    private val userReader: UserReader,
    private val redisService: RedisService,
    private val queuePublisher: ApplicationEventPublisher,
) : Filter {

    @Throws(ServletException::class)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        val uuid = httpRequest.getHeader("Authorization")

        if (uuid == null || uuid.equals("")) {
            sendTokenError(
                httpResponse,
                ApiResponse.of(HttpStatus.UNAUTHORIZED, "사용자 정보를 확인할 수 없습니다.", null),
                uuid
            )
            return
        }

        val user = userReader.findByUUID(uuid)

        if (user == null) {
            sendTokenError(
                httpResponse,
                ApiResponse.of(HttpStatus.UNAUTHORIZED, "존재하지 않는 사용자입니다.", null),
                uuid
            )
            return
        }

        val isComplete = redisService.getHash(Key.COMPLETE.toString(), "complete$uuid")

//        if (isComplete == null && redisService.getZSetRank(Key.WAITING.toString(), uuid) == null) {
//            sendTokenError(
//                httpResponse,
//                ApiResponse.of(
//                    HttpStatus.UNAUTHORIZED,
//                    "대기열에 존재하지 않는 사용자입니다. 새로운 토큰을 발급해주세요.", null
//                ),
//                uuid
//            )
//            return
//        }

        if (isComplete == null) {
            val waitingOrder = redisService.getZSetRank(Key.WAITING.toString(), uuid) ?: 0
            val remainTime =
                waitingOrder!! + 100
            sendTokenError(
                httpResponse,
                ApiResponse.of(
                    HttpStatus.TOO_EARLY,
                    "대기열 순번에 도달하지 않았습니다. 현재 대기 순위는 ${waitingOrder}번 " +
                        "이며 남은 대기 시간은 ${remainTime}m 입니다.",
                    null
                ),
                uuid
            )
            return
        }

        redisService.setExpire("complete$uuid", redisService.expireTime)
//        queuePublisher.publishEvent(QueuePollingEvent(uuid))

        chain?.doFilter(request, response)
    }

    fun sendTokenError(response: HttpServletResponse, obj: ApiResponse<Nothing?>, uuid: String) {
        val data = objectMapper.writeValueAsString(obj)
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.writer.write(data)
        logger.error { "오류가 발생하였습니다. UUID: $uuid, 오류 내용: ${obj.message}" }
    }
}
