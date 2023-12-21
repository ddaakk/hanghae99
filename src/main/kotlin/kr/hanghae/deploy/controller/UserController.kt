package kr.hanghae.deploy.controller

import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.user.request.ChargeBalanceRequest
import kr.hanghae.deploy.dto.user.response.ChargeBalanceResponse
import kr.hanghae.deploy.dto.user.response.GetBalanceResponse
import kr.hanghae.deploy.service.UserService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/api/v1/user/token", produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun generateToken(
        @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") lastEventId: String
    ): ResponseEntity<SseEmitter> {
        return ResponseEntity.ok(userService.generateToken(lastEventId))
    }

    @PutMapping("/api/v1/user/charge")
    fun chargeBalance(
        @RequestBody request: ChargeBalanceRequest,
        @RequestHeader("Authorization") uuid: String,
    ): ApiResponse<ChargeBalanceResponse> {
        return ApiResponse.created(userService.chargeBalance(request, uuid))
    }

    @GetMapping("/api/v1/user/balance")
    fun getBalance(
        @RequestHeader("Authorization") uuid: String,
    ): ApiResponse<GetBalanceResponse> {
        return ApiResponse.ok(userService.getBalance(uuid))
    }
}
