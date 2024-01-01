package kr.hanghae.deploy.dto.seat.request

import java.time.LocalDate

data class SeatRequest(
    val concertNumber: String,
    val date: LocalDate,
)
