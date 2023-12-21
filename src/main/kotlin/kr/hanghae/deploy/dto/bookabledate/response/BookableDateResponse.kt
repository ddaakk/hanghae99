package kr.hanghae.deploy.dto.bookabledate.response

import kr.hanghae.deploy.domain.bookabledate.BookableDate

data class BookableDateResponse(
    val date: String,
    val seatOrders: List<Long>
) {

    companion object {
        fun of(bookableDate: BookableDate): BookableDateResponse {
            return BookableDateResponse(
                date = bookableDate.date,
                seatOrders = bookableDate.seats.map { it.orders }
            )
        }
    }
}
