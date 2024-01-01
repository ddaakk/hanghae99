package kr.hanghae.deploy.dto.bookabledate.response

import kr.hanghae.deploy.domain.BookableDate
import java.time.LocalDate

data class BookableDateResponse(
    val date: LocalDate,
) {

    companion object {
        fun of(bookableDate: BookableDate): BookableDateResponse {
            return BookableDateResponse(
                date = bookableDate.date,
            )
        }
    }
}
