package kr.hanghae.deploy.dto.user.response

import kr.hanghae.deploy.domain.user.User

data class GetBalanceResponse(
    val uuid: String,
    val balance: Long,
) {

    companion object {
        fun of(user: User): GetBalanceResponse {
            return GetBalanceResponse(
                uuid = user.uuid,
                balance = user.balance,
            )
        }
    }
}
