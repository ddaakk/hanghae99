package kr.hanghae.deploy.dto.bookabledate.response

import kr.hanghae.deploy.domain.BookableDate

data class BookableDateResponse(
    val date: String,
    val seatNumbers: List<Int>
) {

    companion object {
        fun of(bookableDate: BookableDate): BookableDateResponse {
            return BookableDateResponse(
                date = bookableDate.date,
                seatNumbers = bookableDate.seats.map { it.number }
            )
        }
    }
}
