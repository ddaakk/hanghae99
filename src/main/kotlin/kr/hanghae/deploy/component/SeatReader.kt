package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.repository.SeatRepository
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.time.LocalDate

@Component
class SeatReader(
    private val seatRepository: SeatRepository,
) {

    fun getByBookableDateId(bookableDateId: Long): List<Seat> {
       return seatRepository.findByBookableDateId(bookableDateId).also {
           if (it.isEmpty()) {
               throw RuntimeException("예약할 좌석이 존재하지 않습니다.")
           }
       }
    }

    fun getByOrderAndDate(seatNumbers: List<Int>, bookableDateId: Long): List<Seat> {
        return seatRepository.findByOrderAndDate(seatNumbers, bookableDateId).also {
            if (it.isEmpty()) {
                throw RuntimeException("예약할 좌석이 존재하지 않습니다.")
            }
        }
    }
}
