package kr.hanghae.deploy.domain

import jakarta.persistence.*

@Entity
@Table
class Seat(
    booking: Booking? = null,
    bookableDateId: Long,
    number: Int,
    grade: String,
    price: Long = 0L,
) : BaseEntity() {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOKING_ID")
    var booking: Booking? = booking
        protected set

    var bookableDateId: Long = bookableDateId
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

    fun updateBooking(booking: Booking?) {
        this.booking = booking
    }
}
