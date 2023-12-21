package kr.hanghae.deploy.dto.user.response

import kr.hanghae.deploy.domain.user.User

data class ChargeBalanceResponse(
    val uuid: String,
    val balance: Long,
) {

    companion object {
        fun of(user: User): ChargeBalanceResponse {
            return ChargeBalanceResponse(
                uuid = user.uuid,
                balance = user.balance,
            )
        }
    }
}
