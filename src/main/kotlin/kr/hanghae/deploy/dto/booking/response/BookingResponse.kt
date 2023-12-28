package kr.hanghae.deploy.dto.booking.response

import kr.hanghae.deploy.domain.Booking

data class BookingResponse(
    val uuid: String,
    val date: String?,
    val totalPrice: Long,
    val bookingNumber: String,
    val seatNumbers: List<Int>,
) {

    companion object {
        fun of(booking: Booking): BookingResponse {
            return BookingResponse(
                uuid = booking.user.uuid,
                date = booking.bookableDate?.date,
                totalPrice = booking.seats.sumOf { it.price },
                bookingNumber = booking.number,
                seatNumbers = booking.seats.map { it.number }.toList()
            )
        }
    }
}
