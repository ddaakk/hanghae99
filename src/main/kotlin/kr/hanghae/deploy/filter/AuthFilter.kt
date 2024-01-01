package kr.hanghae.deploy.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.service.RedisService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class AuthFilter(
    private val objectMapper: ObjectMapper,
    private val userReader: UserReader,
    private val redisService: RedisService,
) : Filter {

    @Throws(ServletException::class)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        val uuid = httpRequest.getHeader("Authorization")

        if (uuid == null || uuid.equals("")) {
            sendTokenError(
                httpResponse,
                ApiResponse.of(HttpStatus.UNAUTHORIZED, "사용자 정보를 확인할 수 없습니다.", null)
            )
            return
        }

        val user = userReader.findByUUID(uuid)

        if (user == null) {
            sendTokenError(
                httpResponse,
                ApiResponse.of(HttpStatus.UNAUTHORIZED, "존재하지 않는 사용자입니다.", null)
            )
            return
        }

        val isComplete = redisService.getComplete(uuid)

        if (isComplete == null && redisService.getQueueOrder(uuid) == null) {
            sendTokenError(
                httpResponse,
                ApiResponse.of(HttpStatus.UNAUTHORIZED,
                    "대기열에 존재하지 않는 사용자입니다. 새로운 토큰을 발급해주세요.", null)
            )
            return
        } else if (isComplete == null) {
            val waitingOrder = redisService.getQueueOrder(uuid)
            val remainTime = waitingOrder!! * 100L
            sendTokenError(
                httpResponse,
                ApiResponse.of(
                    HttpStatus.TOO_EARLY,
                    "대기열 순번에 도달하지 않았습니다. 현재 대기 순위는 ${waitingOrder}번 " +
                        "이며 남은 대기 시간은 ${remainTime}ms 입니다.",
                    null
                )
            )
            return
        }

        redisService.removeComplete(uuid)

        chain?.doFilter(request, response)
    }

    fun sendTokenError(response: HttpServletResponse, obj: Any) {
        val data = objectMapper.writeValueAsString(obj)
        response.characterEncoding = "UTF-8"
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json"
        response.writer.write(data)
    }
}
