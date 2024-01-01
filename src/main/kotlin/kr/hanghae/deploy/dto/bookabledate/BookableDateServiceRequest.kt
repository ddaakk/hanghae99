package kr.hanghae.deploy.dto.bookabledate

import java.time.LocalDate

data class BookableDateServiceRequest(
    val concertNumber: String,
) {
    companion object {
        fun toService(concertNumber: String): BookableDateServiceRequest {
            return BookableDateServiceRequest(
                concertNumber = concertNumber
            )
        }
    }
}
