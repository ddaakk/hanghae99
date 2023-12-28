package kr.hanghae.deploy.component

import kr.hanghae.deploy.repository.BookingSeatRepository
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class BookingSeatsValidator(
    private val bookingSeatRepository: BookingSeatRepository,
) {

    fun validateSeatsBooking(seatIds: List<Long>) {
       bookingSeatRepository.countBySeatIds(seatIds).also {
           if (it > 0) {
               throw RuntimeException("이미 예약중인 좌석이 존재합니다.")
           }
       }
    }
}
