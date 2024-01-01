package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.*
import kr.hanghae.deploy.repository.BookingRepositoryImpl
import org.springframework.stereotype.Component

@Component
class BookingManager(
    private val bookingRepositoryImpl: BookingRepositoryImpl,
) {
    fun requestBooking(user: User, seats: List<Seat>, bookableDate: BookableDate): Booking {
        val booking = Booking(user.id ?: 0, bookableDate.date)
        booking.updateSeats(seats.toMutableList())
        return bookingRepositoryImpl.save(booking)
    }
}
