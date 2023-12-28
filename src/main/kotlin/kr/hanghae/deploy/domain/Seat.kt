package kr.hanghae.deploy.domain

import jakarta.persistence.*
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Booking
import kr.hanghae.deploy.domain.BaseEntity
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Payment

enum class SeatStatus {
    BOOKED,
    BOOKABLE,
    TEMP_HOLD,
}

@Entity
@Table
class Seat(
    bookableDate: BookableDate,
    booking: Booking? = null,
    payment: Payment? = null,
    concert: Concert,
    number: Int,
    grade: String,
    price: Long = 0L,
) : BaseEntity() {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKABLE_DATE_ID")
    var bookableDate: BookableDate = bookableDate
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKING_ID")
    var booking: Booking? = booking
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONCERT_ID")
    var concert: Concert = concert
        protected set

    var grade: String = grade
        protected set

    var number: Int = number
        protected set

    var price: Long = price
        protected set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateBooking(booking: Booking) {
        this.booking = booking
    }
}
