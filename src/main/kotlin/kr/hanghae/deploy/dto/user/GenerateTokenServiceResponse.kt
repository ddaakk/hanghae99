package kr.hanghae.deploy.dto.user

data class GenerateTokenServiceResponse(
    val uuid: String,
    val waiting: Int,
    val remainTime: Int,
) {
    companion object {
        fun from(uuid: String, waiting: Int, remainTime: Int): GenerateTokenServiceResponse {
            return GenerateTokenServiceResponse(
                uuid = uuid,
                waiting = waiting,
                remainTime = remainTime,
            )
        }
    }
}
