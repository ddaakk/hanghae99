package kr.hanghae.deploy.dto.message.response

data class WaitingMessageResponse(
    val waitingOrder: Int,
    val remainTime: Int,
) {

    companion object {
        fun from(waitingOrder: Int): WaitingMessageResponse {
            return WaitingMessageResponse(
                waitingOrder = waitingOrder,
                remainTime = 0,
            )
        }
    }
}
