package kr.hanghae.deploy.exception

import kr.hanghae.deploy.dto.ApiResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import kotlin.IllegalArgumentException

private val logger = KotlinLogging.logger {}

@RestControllerAdvice
class GlobalExceptionAdvice {
    @ExceptionHandler(RuntimeException::class)
    fun handleBaseException(e: RuntimeException): ApiResponse<Nothing?> {
        val message = e.message ?: "메세지가 존재하지 않습니다."
        logger.error { "오류가 발생하였습니다. 오류 내용: $message, ${e.printStackTrace()}" }
        return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, message, null)
    }
}
