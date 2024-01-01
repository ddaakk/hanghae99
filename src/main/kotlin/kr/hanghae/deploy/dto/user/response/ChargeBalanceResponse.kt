package kr.hanghae.deploy.dto.user.response

import kr.hanghae.deploy.domain.User
import java.math.BigDecimal

data class ChargeBalanceResponse(
    val uuid: String,
    val balance: BigDecimal,
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
