package kr.hanghae.deploy.domain

import jakarta.persistence.*

enum class PayStatus {
    PAY_COMPLETE,
    NOT_PAID,
}

@Entity
class Payment(
    booking: Booking,
    status: PayStatus = PayStatus.NOT_PAID,
) : BaseEntity() {

    @OneToOne(fetch = FetchType.LAZY)
    var booking: Booking = booking
        protected set

    @Enumerated(EnumType.STRING)
    var status: PayStatus = status
        protected set

//    val totalPrice: Long = seats.sumOf { it.price },

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
