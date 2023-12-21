package kr.hanghae.deploy.exception

import kr.hanghae.deploy.dto.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import kotlin.IllegalArgumentException

@RestControllerAdvice
class GlobalExceptionAdvice {
    @ExceptionHandler(RuntimeException::class)
    fun handleBaseException(e: RuntimeException): ApiResponse<Nothing?> {
        val message = e.message ?: "메세지가 존재하지 않습니다."
        return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, message, null)
    }
}
