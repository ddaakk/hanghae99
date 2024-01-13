package kr.hanghae.deploy.exception

import jakarta.servlet.http.HttpServletRequest
import kr.hanghae.deploy.dto.ApiResponse
import lombok.extern.log4j.Log4j2
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import kotlin.IllegalArgumentException

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionAdvice {
    @ExceptionHandler(RuntimeException::class)
    fun handleBaseException(e: RuntimeException, request: HttpServletRequest): ApiResponse<Nothing?> {
        val message = e.message ?: "메세지가 존재하지 않습니다."
        logger.error { "오류가 발생하였습니다. UUID: ${request.getHeader("Authorization")}," +
            " 오류 내용: $message, ${e.stackTrace[0]}, 요청 주소: ${request.requestURI}" }
        return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, message, null)
    }
}
