package kr.hanghae.deploy.domain

import com.fasterxml.uuid.Generators
import jakarta.persistence.*
import java.math.BigInteger


enum class BookingStatus {
    RESERVING,
    RESERVED,
    CANCELED,
}

@Entity
@Table
class Booking(
    user: User,
    seats: MutableList<Seat> = mutableListOf(),
    bookableDate: BookableDate?,
    status: BookingStatus = BookingStatus.RESERVING,
    payment: Payment? = null,
    number: String = String.format(
        "%040d",
        BigInteger(
            Generators.timeBasedGenerator().generate().toString().replace("-", ""), 16
        )
    )
) : BaseEntity() {

    @ManyToOne(fetch = FetchType.LAZY)
    var user: User = user
        protected set

    @OneToMany(mappedBy = "booking", cascade = [CascadeType.ALL], orphanRemoval = true)
    var seats: MutableList<Seat> = seats
        protected set

    @OneToOne(fetch = FetchType.LAZY)
    var bookableDate: BookableDate? = bookableDate
        protected set

    @Enumerated(EnumType.STRING)
    var status: BookingStatus = status
        protected set

    @OneToOne(mappedBy = "booking", cascade = [CascadeType.ALL], orphanRemoval = true)
    var payment: Payment? = payment
        protected set

    var number: String = number
        protected set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updatePayment(payment: Payment) {
        this.payment = payment
        this.status = BookingStatus.RESERVED
    }

    fun getTotalPrice(): Long {
        return seats.sumOf { it.price }
    }
}
