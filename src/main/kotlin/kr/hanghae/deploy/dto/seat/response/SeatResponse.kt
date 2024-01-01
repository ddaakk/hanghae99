package kr.hanghae.deploy.dto.seat.response

import kr.hanghae.deploy.domain.*
import kr.hanghae.deploy.dto.seat.SeatServiceResponse
import java.time.LocalDate

data class SeatResponse(
    val seatNumber: Int,
    val price: Long,
    val concertName: String,
    val bookingStatus: BookingStatus?,
    val date: LocalDate,
) {

    companion object {
        fun of(response: SeatServiceResponse): List<SeatResponse> {
            val (bookableDate, seats) = response
            val responses = mutableListOf<SeatResponse>()

            seats.map {
                responses.add(
                    SeatResponse(
                        seatNumber = it.number,
                        price = it.price,
                        concertName = bookableDate.concert.name,
                        bookingStatus = it.booking?.status ?: BookingStatus.NOT_BOOKED,
                        date = bookableDate.date
                    )
                )
            }

            return responses
        }
    }
}
