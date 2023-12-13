package kr.hanghae.deploy.domain.seat

import jakarta.persistence.*
import kr.hanghae.deploy.domain.bookabledate.BookableDate
import kr.hanghae.deploy.domain.booking.Booking
import kr.hanghae.deploy.domain.common.BaseEntity
import kr.hanghae.deploy.domain.payment.Payment

@Entity
class Seat (

    @ManyToOne(fetch = FetchType.LAZY)
    val bookableDate: BookableDate,

    @ManyToOne(fetch = FetchType.LAZY)
    val booking: Booking?,

    @ManyToOne(fetch = FetchType.LAZY)
    val payment: Payment?,

    val orders: Long = 1L,

    val price: Long = 0L,

    @Enumerated(EnumType.STRING)
    val seatStatus: SeatStatus = SeatStatus.BOOKABLE,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

) : BaseEntity() {
}
