package kr.hanghae.deploy.dto.user

import java.math.BigDecimal

data class ChargeBalanceServiceRequest(
    val balance: BigDecimal,
    val uuid: String,
) {
    companion object {
        fun toService(balance: BigDecimal, uuid: String): ChargeBalanceServiceRequest {
            return ChargeBalanceServiceRequest(
                balance = balance,
                uuid = uuid,
            )
        }
    }
}
