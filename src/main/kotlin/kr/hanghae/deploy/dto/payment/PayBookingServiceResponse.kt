package kr.hanghae.deploy.dto.payment

import kr.hanghae.deploy.domain.Booking

data class PayBookingServiceResponse(
    val uuid: String,
    val booking: Booking,
) {
    companion object {
        fun from(uuid: String, booking: Booking): PayBookingServiceResponse {
            return PayBookingServiceResponse(
                uuid = uuid,
                booking = booking,
            )
        }
    }
}
