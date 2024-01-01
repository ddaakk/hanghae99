package kr.hanghae.deploy.dto.payment.response

import kr.hanghae.deploy.domain.Booking
import kr.hanghae.deploy.domain.BookingStatus
import kr.hanghae.deploy.dto.payment.PayBookingServiceResponse

data class PaymentResponse(
    val uuid: String,
    val bookingNumber: String,
    val seatNumbers: List<Int> = mutableListOf(),
    val totalPrice: Long,
    val bookingStatus: BookingStatus,
) {

    companion object {
        fun from(response: PayBookingServiceResponse): PaymentResponse {
            val (uuid, booking) = response
            return PaymentResponse(
                uuid = uuid,
                bookingNumber = booking.number,
                seatNumbers = booking.seats.map { it.number }.toList(),
                totalPrice = booking.seats.sumOf { it.price },
                bookingStatus = booking.status,
            )
        }
    }
}
