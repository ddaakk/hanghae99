package kr.hanghae.deploy.dto.user.response

import kr.hanghae.deploy.domain.user.User

data class ApplyTokenResponse(
    val token: String,
    val waitingOrder: String,
    val remainTime: String,
) {

    companion object {
        fun of(user: User): ApplyTokenResponse {
            return ApplyTokenResponse(
                token = user.token,
                waitingOrder = "0",
                remainTime = "0",
            )
        }
    }
}
