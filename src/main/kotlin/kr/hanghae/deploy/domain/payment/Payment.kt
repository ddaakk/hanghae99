package kr.hanghae.deploy.domain.payment

import jakarta.persistence.*
import kr.hanghae.deploy.domain.booking.Booking
import kr.hanghae.deploy.domain.common.BaseEntity

@Entity
class Payment(
    @OneToOne(fetch = FetchType.LAZY)
    val booking: Booking,

    @Enumerated(EnumType.STRING)
    val payStatus: PayStatus = PayStatus.BOOKED,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

) : BaseEntity() {
}
