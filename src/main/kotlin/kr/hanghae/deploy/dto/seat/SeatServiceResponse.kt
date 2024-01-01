package kr.hanghae.deploy.dto.seat

import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Seat
import java.time.LocalDate

data class SeatServiceResponse(
    val bookableDate: BookableDate,
    val seats: List<Seat>,
) {
    companion object {
        fun from(bookableDate: BookableDate, seats: List<Seat>): SeatServiceResponse {
            return SeatServiceResponse(
                bookableDate = bookableDate,
                seats = seats,
            )
        }
    }
}
