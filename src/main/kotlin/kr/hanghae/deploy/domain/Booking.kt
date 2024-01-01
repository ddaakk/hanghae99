package kr.hanghae.deploy.domain

import com.fasterxml.uuid.Generators
import jakarta.persistence.*
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate


enum class BookingStatus {
    BOOKING,
    BOOKED,
    NOT_BOOKED,
    CANCELED,
}

@Entity
@Table
class Booking(
    userId: Long,
    date: LocalDate,
    seats: MutableList<Seat> = mutableListOf(),
    status: BookingStatus = BookingStatus.BOOKING,
    number: String = String.format(
        "%040d",
        BigInteger(
            Generators.timeBasedGenerator().generate().toString().replace("-", ""), 16
        )
    ),
) : BaseEntity() {

    var userId: Long = userId
        protected set

    var date: LocalDate = date
        protected set

    @OneToMany(mappedBy = "booking")
    var seats: MutableList<Seat> = seats
        protected set

    @Enumerated(EnumType.STRING)
    var status: BookingStatus = status
        protected set

    var number: String = number
        protected set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateSeats(seats: MutableList<Seat>) {
        this.seats = seats
        seats.forEach { it.updateBooking(this) }
    }

    fun getTotalPrice(): BigDecimal {
        return this.seats.sumOf { it.price }.toBigDecimal()
    }

    fun changeToBooked() {
        this.status = BookingStatus.BOOKED
    }

    fun changeToBookable() {
        this.status = BookingStatus.NOT_BOOKED
        this.seats.forEach { it.updateBooking(null) }
    }
}
