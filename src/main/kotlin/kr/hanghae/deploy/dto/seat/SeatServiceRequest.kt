package kr.hanghae.deploy.dto.seat

data class SeatServiceRequest(
    val date: String,
) {
    companion object {
        fun toService(date: String): SeatServiceRequest {
            return SeatServiceRequest(
                date = date
            )
        }
    }
}
