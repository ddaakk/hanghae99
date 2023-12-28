package kr.hanghae.deploy.dto.user

data class ChargeBalanceServiceRequest(
    val balance: Long,
    val uuid: String,
) {
    companion object {
        fun toService(balance: Long, uuid: String): ChargeBalanceServiceRequest {
            return ChargeBalanceServiceRequest(
                balance = balance,
                uuid = uuid,
            )
        }
    }
}
