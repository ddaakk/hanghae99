package kr.hanghae.deploy.dto.booking

data class BookingServiceRequest(
    val date: String,
    val seatNumbers: List<Int>,
    val uuid: String,
) {
    companion object {
        fun toService(date: String, seatNumbers: List<Int>, uuid: String): BookingServiceRequest {
            return BookingServiceRequest(
                date = date,
                seatNumbers = seatNumbers,
                uuid = uuid,
            )
        }
    }
}
