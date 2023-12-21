package kr.hanghae.deploy.domain.booking

import jakarta.persistence.*
import kr.hanghae.deploy.domain.bookabledate.BookableDate
import kr.hanghae.deploy.domain.common.BaseEntity
import kr.hanghae.deploy.domain.payment.Payment
import kr.hanghae.deploy.domain.seat.Seat
import kr.hanghae.deploy.domain.user.User

@Entity
class Booking (
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,

    @OneToMany(mappedBy = "booking", cascade = [CascadeType.ALL], orphanRemoval = true)
    val seats: MutableList<Seat> = mutableListOf(),

    @OneToOne(fetch = FetchType.LAZY)
    val bookableDate: BookableDate?,

    @OneToOne(mappedBy = "booking", cascade = [CascadeType.ALL], orphanRemoval = true)
    var payment: Payment? = null,

    val number: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity() {

    fun updatePayment(payment: Payment) {
        this.payment = payment
    }
}
