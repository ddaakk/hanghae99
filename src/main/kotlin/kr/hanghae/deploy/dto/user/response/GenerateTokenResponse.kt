package kr.hanghae.deploy.dto.user.response

import kr.hanghae.deploy.domain.user.User

data class GenerateTokenResponse(
    val uuid: String,
    val waitingOrder: Int,
    val remainTime: Int,
) {

    companion object {
        fun from(uuid: String, waitingOrder: Int): GenerateTokenResponse {
            return GenerateTokenResponse(
                uuid = uuid,
                waitingOrder = waitingOrder,
                remainTime = 0,
            )
        }
    }
}
