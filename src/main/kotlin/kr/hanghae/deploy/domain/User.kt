package kr.hanghae.deploy.domain

import com.fasterxml.uuid.Generators
import jakarta.persistence.*
import kr.hanghae.deploy.domain.Booking
import kr.hanghae.deploy.domain.BaseEntity

@Entity
@Table(name = "USERS")
class User(
    uuid: String = Generators.timeBasedGenerator().generate().toString(),
    balance: Long = 0L,
    booking: MutableList<Booking> = mutableListOf()
) : BaseEntity() {


    var uuid: String = uuid
        protected set

    var balance: Long = balance
        protected set

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val booking: MutableList<Booking> = booking

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun chargeBalance(balance: Long) {
        this.balance += balance
    }

    fun payBookingSeats(totalPrice: Long) {
        if (totalPrice > balance) {
            throw RuntimeException("좌석을 구매할 잔액이 부족합니다.")
        }

        this.balance -= balance
    }
}
