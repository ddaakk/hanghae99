package kr.hanghae.deploy.dto.payment.response

import kr.hanghae.deploy.domain.PayStatus
import kr.hanghae.deploy.domain.Payment

data class PaymentResponse(
    val uuid: String,
    val bookingNumber: String,
    val seatNumbers: List<Int> = mutableListOf(),
    val totalPrice: Long,
    val paymentStatus: PayStatus
    ) {

    companion object {
        fun of(payment: Payment): PaymentResponse {
            return PaymentResponse(
                uuid = payment.booking.user.uuid,
                bookingNumber = payment.booking.number,
                seatNumbers = payment.booking.seats.map { it.number }.toList(),
                totalPrice = payment.booking.seats.sumOf { it.price },
                paymentStatus = payment.status,
            )
        }
    }
}
