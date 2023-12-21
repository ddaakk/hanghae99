package kr.hanghae.deploy.dto.booking.response

import kr.hanghae.deploy.domain.booking.Booking
import kr.hanghae.deploy.domain.payment.PayStatus
import kr.hanghae.deploy.domain.seat.Seat
import kr.hanghae.deploy.domain.seat.SeatStatus

data class BookingResponse(
    val uuid: String,
    val date: String?,
    val totalPrice: Long,
    val bookingNumber: String,
    val seatOrder: List<Long>,
) {

    companion object {
        fun of(booking: Booking): BookingResponse {
            return BookingResponse(
                uuid = booking.user.uuid,
                date = booking.bookableDate?.date,
                totalPrice = booking.seats.sumOf { it.price },
                bookingNumber = booking.number,
                seatOrder = booking.seats.map { it.orders }.toList()
            )
        }
    }
}
