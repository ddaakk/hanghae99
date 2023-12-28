package kr.hanghae.deploy.dto.user.response

import kr.hanghae.deploy.domain.User

data class GenerateTokenResponse(
    val uuid: String,
    val waiting: Int,
    val remainTime: Int,
) {

    companion object {
        fun of(user: User): GenerateTokenResponse {
            return GenerateTokenResponse(
                uuid = user.uuid,
                waiting = user.waiting,
                remainTime = 0,
            )
        }
    }
}
