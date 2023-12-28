package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.*
import kr.hanghae.deploy.repository.BookingRepositoryImpl
import kr.hanghae.deploy.repository.BookingSeatRepositoryImpl
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class BookingManager(
    private val bookingRepositoryImpl: BookingRepositoryImpl,
    private val bookingSeatRepositoryImpl: BookingSeatRepositoryImpl,
) {
    @Transactional
    fun requestBooking(user: User, seats: MutableList<Seat>, bookableDate: BookableDate): Booking {
        val booking = Booking(user, seats, bookableDate)
        bookingRepositoryImpl.saveAndFlush(booking)

        // TODO("INSERT, UPDATE 여러 개 나가는데 개선 방법 고민... batch로 나가도록 해야하나?")
        val bookingSeats: MutableList<BookingSeat> = mutableListOf()
        seats.forEach {
            bookingSeats.add(BookingSeat(booking, it))
            it.updateBooking(booking)
        }
        bookingSeatRepositoryImpl.saveAllAndFlush(bookingSeats)

        return booking
    }
}
