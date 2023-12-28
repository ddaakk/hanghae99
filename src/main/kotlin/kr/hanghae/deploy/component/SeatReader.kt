package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.repository.SeatRepository
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class SeatReader(
    private val seatRepository: SeatRepository,
) {

    fun getByDate(date: String): List<Seat> {
       return seatRepository.findAllByDate(date).also {
           if (it.isEmpty()) {
               throw RuntimeException("예약할 좌석이 존재하지 않습니다.")
           }
       }
    }

    fun getByOrderAndDate(seatNumbers: List<Int>, date: String): MutableList<Seat> {
        return seatRepository.findByOrderAndDate(seatNumbers, date).also {
            if (it.isEmpty()) {
                throw RuntimeException("예약할 좌석이 존재하지 않습니다.")
            }
        }
    }
}
