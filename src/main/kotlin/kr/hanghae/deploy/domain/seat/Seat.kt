package kr.hanghae.deploy.domain.seat

import jakarta.persistence.*
import kr.hanghae.deploy.domain.bookabledate.BookableDate
import kr.hanghae.deploy.domain.booking.Booking
import kr.hanghae.deploy.domain.common.BaseEntity

@Entity
class Seat (

    @ManyToOne(fetch = FetchType.LAZY)
    val bookableDate: BookableDate,

    @ManyToOne(fetch = FetchType.LAZY)
    val booking: Booking?,

    @Enumerated(EnumType.STRING)
    val seatStatus: SeatStatus = SeatStatus.BOOKABLE,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

) : BaseEntity() {
}
