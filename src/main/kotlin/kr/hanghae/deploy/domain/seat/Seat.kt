package kr.hanghae.deploy.domain.seat

import jakarta.persistence.*
import kr.hanghae.deploy.domain.bookabledate.BookableDate
import kr.hanghae.deploy.domain.booking.Booking
import kr.hanghae.deploy.domain.common.BaseEntity
import kr.hanghae.deploy.domain.payment.Payment
import kr.hanghae.deploy.domain.user.User

enum class SeatStatus {
    BOOKED,
    BOOKABLE,
    TEMP_HOLD,
}

@Entity
@Table
class Seat (
    bookableDate: BookableDate,
    booking: Booking? = null,
    payment: Payment? = null,
    orders: Long,
    price: Long = 0L,
    seatStatus: SeatStatus = SeatStatus.BOOKABLE,
    user: User? = null,
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
    @JoinColumn(name = "PAYMENT_ID")
    var payment: Payment? = payment
        protected set

    var orders: Long = orders
        protected set

    var price: Long = price
        protected set

    @Enumerated(EnumType.STRING)
    var seatStatus: SeatStatus = seatStatus
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    var user: User? = user
        protected set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun changeToBooking() {
        this.seatStatus = SeatStatus.BOOKED
    }

    fun changeToBookable() {
        this.seatStatus = SeatStatus.BOOKABLE
    }
}
