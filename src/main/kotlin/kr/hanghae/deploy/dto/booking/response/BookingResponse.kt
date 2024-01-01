package kr.hanghae.deploy.dto.booking.response

import kr.hanghae.deploy.dto.booking.BookingServiceResponse
import java.time.LocalDate

data class BookingResponse(
    val uuid: String,
    val date: LocalDate,
    val totalPrice: Long,
    val bookingNumber: String,
    val seatNumbers: List<Int>,
) {

    companion object {
        fun of(response: BookingServiceResponse): BookingResponse {
            val (uuid, booking) = response
            return BookingResponse(
                uuid = uuid,
                date = booking.date,
                totalPrice = booking.seats.sumOf { it.price },
                bookingNumber = booking.number,
                seatNumbers = booking.seats.map { it.number }.toList()
            )
        }
    }
}
