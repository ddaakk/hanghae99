package kr.hanghae.deploy.domain

import com.fasterxml.uuid.Generators
import jakarta.persistence.*
import lombok.EqualsAndHashCode
import java.math.BigDecimal

@Entity
@Table(name = "USERS")
@EqualsAndHashCode(callSuper = false)
class User(
    uuid: String,
    balance: BigDecimal = BigDecimal(0),
) : BaseEntity() {


    val uuid: String = uuid

    var balance: BigDecimal = balance
        protected set

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

    @Version
    var version: Long? = 0L

}
