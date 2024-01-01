package kr.hanghae.deploy.dto.booking.request

import java.time.LocalDate

data class BookingRequest(
    val concertNumber: String,
    val date: LocalDate,
    val seatNumbers: List<Int>,
)

