package kr.hanghae.deploy.domain

import jakarta.persistence.*

@Entity
@Table
class BookingSeat(
    booking: Booking,
    seat: Seat,
) : BaseEntity() {
    @ManyToOne(fetch = FetchType.LAZY)
    var booking: Booking = booking
        protected set

    @OneToOne(fetch = FetchType.LAZY)
    var seat: Seat = seat
        protected set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
