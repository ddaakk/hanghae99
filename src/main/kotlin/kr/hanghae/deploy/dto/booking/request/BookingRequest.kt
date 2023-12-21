package kr.hanghae.deploy.dto.booking.request

import org.springframework.web.bind.annotation.RequestHeader

data class BookingRequest(
    val date: String,
    val seatOrder: List<Long>,
)

