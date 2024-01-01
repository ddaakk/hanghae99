package kr.hanghae.deploy.dto.booking

import kr.hanghae.deploy.domain.Booking
import java.time.LocalDate

data class BookingServiceResponse(
    val uuid: String,
    val booking: Booking,
) {
    companion object {
        fun from(uuid: String, booking: Booking): BookingServiceResponse {
            return BookingServiceResponse(
                uuid = uuid,
                booking = booking,
            )
        }
    }
}
