package kr.hanghae.deploy.dto.payment

data class PayBookingServiceRequest(
    val bookingNumber: String,
    val uuid: String,
) {
    companion object {
        fun toService(bookingNumber: String, uuid: String): PayBookingServiceRequest {
            return PayBookingServiceRequest(
                bookingNumber = bookingNumber,
                uuid = uuid,
            )
        }
    }
}
