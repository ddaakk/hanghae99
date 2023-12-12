package kr.hanghae.deploy.domain.payment

import jakarta.persistence.*
import kr.hanghae.deploy.domain.booking.Booking
import kr.hanghae.deploy.domain.common.BaseEntity
import kr.hanghae.deploy.domain.seat.Seat

@Entity
class Payment(
    @OneToOne(fetch = FetchType.LAZY)
    val booking: Booking,

    @Enumerated(EnumType.STRING)
    val payStatus: PayStatus = PayStatus.NOT_PAID,

    @OneToMany(mappedBy = "payment", cascade = [CascadeType.ALL], orphanRemoval = true)
    val seats: MutableList<Seat> = mutableListOf(),

    val totalPrice: Long = 0L,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

) : BaseEntity() {
}
