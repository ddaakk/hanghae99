package kr.hanghae.deploy.service

import kr.hanghae.deploy.component.SeatReader
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.dto.seat.SeatServiceRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class SeatService(
    private val seatReader: SeatReader,
) {
    fun getSeatsByDate(request: SeatServiceRequest): List<Seat> {
        return seatReader.getByDate(date = request.date)
    }
}
