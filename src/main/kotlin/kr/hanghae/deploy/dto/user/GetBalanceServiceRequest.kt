package kr.hanghae.deploy.dto.user

data class GetBalanceServiceRequest(
    val uuid: String,
) {
    companion object {
        fun toService(uuid: String): GetBalanceServiceRequest {
            return GetBalanceServiceRequest(
                uuid = uuid,
            )
        }
    }
}
