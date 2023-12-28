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
        val booking = Booking(user, seats, bookableDate) // 예약 엔티티 생성
        bookingRepositoryImpl.saveAndFlush(booking) // flush 쿼리 날리기

        // TODO("INSERT, UPDATE 여러 개 나가는데 개선 방법 고민... batch로 나가도록 해야하나?")
        val bookingSeats: MutableList<BookingSeat> = mutableListOf()
        seats.forEach {
            bookingSeats.add(BookingSeat(booking, it)) // insert 여러번
            it.updateBooking(booking) // update 여러번
        }
        bookingSeatRepositoryImpl.saveAllAndFlush(bookingSeats)

        return booking
    }
}
