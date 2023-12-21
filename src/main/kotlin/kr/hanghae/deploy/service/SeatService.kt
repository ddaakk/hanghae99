package kr.hanghae.deploy.service

import kr.hanghae.deploy.component.SeatReader
import kr.hanghae.deploy.dto.seat.SeatServiceRequest
import kr.hanghae.deploy.dto.seat.response.SeatResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
@Transactional(readOnly = true)
class SeatService(
    private val seatReader: SeatReader,
) {
    fun getSeatsByDate(request: SeatServiceRequest): List<SeatResponse> {
        val seats = seatReader.readerByDate(date = request.date)
        return seats.stream()
            .map(SeatResponse::of)
            .collect(Collectors.toList())
    }
}
