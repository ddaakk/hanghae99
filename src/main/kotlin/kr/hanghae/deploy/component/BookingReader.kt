package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.Booking
import kr.hanghae.deploy.repository.BookingRepository
import org.springframework.stereotype.Component

@Component
class BookingReader(
    private val bookingRepository: BookingRepository,
) {

    fun getByBookingNumber(bookingNumber: String, userId: Long): Booking {
        return bookingRepository.findByBookingNumber(bookingNumber, userId)
            ?: throw RuntimeException("존재하지 않는 예약 번호입니다.")
    }

}
