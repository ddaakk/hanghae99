package kr.hanghae.deploy.dto.booking

import java.time.LocalDate

data class BookingServiceRequest(
    val concertNumber: String,
    val date: LocalDate,
    val seatNumbers: List<Int>,
    val uuid: String,
) {
    companion object {
        fun toService(
            concertNumber: String,
            date: LocalDate,
            seatNumbers: List<Int>,
            uuid: String
        ): BookingServiceRequest {
            return BookingServiceRequest(
                concertNumber = concertNumber,
                date = date,
                seatNumbers = seatNumbers.sorted(),
                uuid = uuid,
            )
        }
    }
}
