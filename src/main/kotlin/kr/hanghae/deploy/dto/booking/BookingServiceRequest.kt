package kr.hanghae.deploy.dto.booking

data class BookingServiceRequest(
    val date: String,
    val seatOrder: List<Long>,
    val uuid: String,
) {
    companion object {
        fun toService(date: String, seatOrder: List<Long>, uuid: String): BookingServiceRequest {
            return BookingServiceRequest(
                date = date,
                seatOrder = seatOrder,
                uuid = uuid,
            )
        }
    }
}
