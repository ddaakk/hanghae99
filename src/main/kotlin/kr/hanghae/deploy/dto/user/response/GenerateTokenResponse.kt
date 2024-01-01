package kr.hanghae.deploy.dto.user.response

import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.dto.user.GenerateTokenServiceResponse

data class GenerateTokenResponse(
    val uuid: String,
    val waiting: Int,
    val remainTime: Int,
) {

    companion object {
        fun of(response: GenerateTokenServiceResponse): GenerateTokenResponse {
            val (uuid, waiting, remainTime) = response
            return GenerateTokenResponse(
                uuid = uuid,
                waiting = waiting,
                remainTime = remainTime,
            )
        }
    }
}
