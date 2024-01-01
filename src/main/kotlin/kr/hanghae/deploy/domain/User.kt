package kr.hanghae.deploy.domain

import com.fasterxml.uuid.Generators
import jakarta.persistence.*
import kr.hanghae.deploy.domain.Booking
import kr.hanghae.deploy.domain.BaseEntity
import java.math.BigDecimal

@Entity
@Table(name = "USERS")
class User(
    uuid: String = Generators.timeBasedGenerator().generate().toString(),
    balance: BigDecimal = BigDecimal(0),
//    waiting: Int = 1,
) : BaseEntity() {


    var uuid: String = uuid
        protected set

    var balance: BigDecimal = balance
        protected set

//    var waiting: Int = waiting
//        protected set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun chargeBalance(balance: BigDecimal) {
        this.balance += balance
    }

    fun payBooking(totalPrice: BigDecimal) {
        if (totalPrice > balance) {
            throw RuntimeException("좌석을 구매할 잔액이 부족합니다.")
        }

        this.balance -= totalPrice
    }

//    fun updateWaiting(waiting: Int) {
//        this.waiting = waiting
//    }
}
