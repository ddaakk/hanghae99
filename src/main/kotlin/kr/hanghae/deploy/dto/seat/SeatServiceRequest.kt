package kr.hanghae.deploy.dto.seat

import java.time.LocalDate

data class SeatServiceRequest(
    val concertNumber: String,
    val date: LocalDate,
) {
    companion object {
        fun toService(concertNumber: String, date: LocalDate): SeatServiceRequest {
            return SeatServiceRequest(
                concertNumber = concertNumber,
                date = date,
            )
        }
    }
}
