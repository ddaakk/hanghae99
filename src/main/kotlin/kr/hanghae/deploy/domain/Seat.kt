package kr.hanghae.deploy.domain

import jakarta.persistence.*
import lombok.EqualsAndHashCode

@Entity
@Table
@EqualsAndHashCode(callSuper = false)
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

    val bookableDateId: Long = bookableDateId

    val grade: String = grade

    val number: Int = number

    val price: Long = price

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateBooking(booking: Booking?) {
        this.booking = booking
    }
}
