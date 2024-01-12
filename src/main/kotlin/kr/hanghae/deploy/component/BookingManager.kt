package kr.hanghae.deploy.component

import com.fasterxml.uuid.Generators
import kr.hanghae.deploy.domain.*
import kr.hanghae.deploy.repository.BookingRepositoryImpl
import org.springframework.stereotype.Component
import java.math.BigInteger

@Component
class BookingManager(
    private val bookingRepositoryImpl: BookingRepositoryImpl,
) {
    fun requestBooking(user: User, seats: List<Seat>, bookableDate: BookableDate): Booking {
        val number = String.format(
            "%040d",
            BigInteger(
                Generators.timeBasedGenerator().generate().toString().replace("-", ""), 16
            )
        )
        val booking = Booking(userId = user.id ?: 0, date = bookableDate.date, number = number)
        booking.updateSeats(seats.toMutableList())
        return bookingRepositoryImpl.save(booking)
    }
}
