package kr.hanghae.deploy.dto.booking.request

data class BookingRequest(
    val date: String,
    val seatNumbers: List<Int>,
)

