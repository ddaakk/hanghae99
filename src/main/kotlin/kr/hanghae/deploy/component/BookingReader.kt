package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Booking
import kr.hanghae.deploy.repository.BookableDateRepository
import kr.hanghae.deploy.repository.BookingRepository
import kr.hanghae.deploy.repository.BookingRepositoryImpl
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class BookingReader(
    private val bookingRepository: BookingRepository,
) {

    fun getByBookingNumber(bookingNumber: String, uuid: String): Booking {
        return bookingRepository.findByBookingNumber(bookingNumber, uuid)
            ?: throw RuntimeException("존재하지 않는 예약 번호입니다.")
    }

}
