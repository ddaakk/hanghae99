package kr.hanghae.deploy.dto.user.response

import kr.hanghae.deploy.domain.User

data class GenerateTokenResponse(
    val uuid: String,
    val waitingOrder: Int,
    val remainTime: Int,
) {

    companion object {
        fun of(user: User): GenerateTokenResponse {
            return GenerateTokenResponse(
                uuid = user.uuid,
                waitingOrder = 0,
                remainTime = 0,
            )
        }


        fun from(uuid: String, waitingOrder: Int): GenerateTokenResponse {
            return GenerateTokenResponse(
                uuid = uuid,
                waitingOrder = waitingOrder,
                remainTime = 0,
            )
        }
    }
}
