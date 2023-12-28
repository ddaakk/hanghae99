package kr.hanghae.deploy.dto.seat.response

import kr.hanghae.deploy.domain.*

data class SeatResponse(
    val seatNumber: Int,
    val price: Long,
    val concertName: String,
    val bookingStatus: BookingStatus?,
    val date: String,
) {

    companion object {
        fun of(seat: Seat): SeatResponse {
            return SeatResponse(
                seatNumber = seat.number,
                price = seat.price,
                concertName = seat.concert.name,
                bookingStatus = seat.booking?.status,
                date = seat.bookableDate.date,
            )
        }
    }
}
