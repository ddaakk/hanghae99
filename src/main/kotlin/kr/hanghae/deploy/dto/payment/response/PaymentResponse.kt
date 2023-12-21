package kr.hanghae.deploy.dto.payment.response

import kr.hanghae.deploy.domain.booking.Booking
import kr.hanghae.deploy.domain.payment.PayStatus
import kr.hanghae.deploy.domain.payment.Payment
import kr.hanghae.deploy.dto.booking.response.BookingResponse

data class PaymentResponse(
    val uuid: String,
    val bookingNumber: String,
    val seatOrder: List<Long> = mutableListOf(),
    val totalPrice: Long,
    val paymentStatus: PayStatus
    ) {

    companion object {
        fun of(payment: Payment): PaymentResponse {
            return PaymentResponse(
                uuid = payment.user.uuid,
                bookingNumber = payment.booking.number,
                seatOrder = payment.seats.map { it.orders }.toList(),
                totalPrice = payment.seats.sumOf { it.price },
                paymentStatus = payment.payStatus,
            )
        }
    }
}
