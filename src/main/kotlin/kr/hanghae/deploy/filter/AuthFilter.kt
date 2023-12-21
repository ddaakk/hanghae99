package kr.hanghae.deploy.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.hanghae.deploy.domain.emitter.EmitterRepository
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.service.UserService
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class AuthFilter(
    private val userService: UserService,
    private val objectMapper: ObjectMapper,
    private val emitterRepository: EmitterRepository,
) : Filter {

    @Throws(ServletException::class)
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        //TODO("IP 확인으로 토큰 중복 발행 검사")

        val token = httpRequest.getHeader("Authorization")

        if (token == null || token.equals("")) {
            sendTokenError(httpResponse,
                ApiResponse.of(HttpStatus.UNAUTHORIZED, "사용자 정보를 확인할 수 없습니다.", null))
            return
        }

        if (!userService.verifyUser(token)) {
            sendTokenError(httpResponse,
                ApiResponse.of(HttpStatus.UNAUTHORIZED, "인증되지 않은 사용자입니다.", null))
            return
        }

        if (emitterRepository.getFinishEmitters().filterKeys { it.startsWith(token) }.isEmpty()) {
            sendTokenError(httpResponse,
                ApiResponse.of(HttpStatus.TOO_EARLY, "아직 대기열 순번에 도달하지 않았습니다.", null))
            return
        }

        userService.deleteUser(token)
        emitterRepository.deleteFinishById(token)

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
