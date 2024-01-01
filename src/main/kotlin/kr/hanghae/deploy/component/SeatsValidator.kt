package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.Seat
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class SeatsValidator() {
    fun validate(seatNumbers: List<Int>, seats: List<Seat>) {
        if (!seats.all { seat -> seatNumbers.contains(seat.number) }) {
            throw RuntimeException("잘못된 번호의 좌석이 존재합니다.")
        }

        if (!seats.all { seat -> seat.booking == null }) {
            throw RuntimeException("이미 예약중인 좌석이 존재합니다.")
        }
    }
}
