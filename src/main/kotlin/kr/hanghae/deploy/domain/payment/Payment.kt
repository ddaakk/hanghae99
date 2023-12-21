package kr.hanghae.deploy.domain.payment

import jakarta.persistence.*
import kr.hanghae.deploy.domain.booking.Booking
import kr.hanghae.deploy.domain.common.BaseEntity
import kr.hanghae.deploy.domain.seat.Seat
import kr.hanghae.deploy.domain.user.User

@Entity
class Payment(
    @OneToOne(fetch = FetchType.LAZY)
    val booking: Booking,

    @Enumerated(EnumType.STRING)
    var payStatus: PayStatus = PayStatus.NOT_PAID,

    @OneToOne(fetch = FetchType.LAZY)
    val user: User,

    @OneToMany(mappedBy = "payment", cascade = [CascadeType.ALL], orphanRemoval = true)
    val seats: MutableList<Seat> = mutableListOf(),

    val totalPrice: Long = 0L,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

) : BaseEntity() {
}
