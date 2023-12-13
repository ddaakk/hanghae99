package kr.hanghae.deploy.controller.user

import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.user.response.ApplyTokenResponse
import kr.hanghae.deploy.service.user.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService,
) {

    @PostMapping("/api/v1/user/token")
    fun applyToken(): ApiResponse<ApplyTokenResponse> {
        return ApiResponse.ok(userService.applyToken())
    }
}
