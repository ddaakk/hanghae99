package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.seat.Seat
import kr.hanghae.deploy.domain.seat.SeatRepository
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class SeatReader(
    private val seatRepository: SeatRepository,
) {

    fun readerByDate(date: String): List<Seat> {
       return seatRepository.findAllByDate(date).also {
           if (it.isEmpty()) {
               throw RuntimeException("예약할 좌석이 존재하지 않습니다.")
           }
       }
    }

    fun readerByOrderAndDate(seatOrders: List<Long>, date: String): MutableList<Seat> {
        return seatRepository.findByOrderAndDate(seatOrders, date).also {
            if (it.isEmpty()) {
                throw RuntimeException("예약할 좌석이 존재하지 않습니다.")
            }
        }
    }
}
