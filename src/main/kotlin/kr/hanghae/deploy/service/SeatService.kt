package kr.hanghae.deploy.service

import kr.hanghae.deploy.component.BookableDateReader
import kr.hanghae.deploy.component.ConcertReader
import kr.hanghae.deploy.component.SeatReader
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.dto.seat.SeatServiceRequest
import kr.hanghae.deploy.dto.seat.SeatServiceResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SeatService(
    private val seatReader: SeatReader,
    private val bookableDateReader: BookableDateReader,
) {
    @Transactional(readOnly = true)
    fun getSeatsByConcertAndDate(request: SeatServiceRequest): SeatServiceResponse {
        val (concertNumber, date) = request
        val bookableDate = bookableDateReader.getByConcertAndDate(concertNumber, date)
        val seats = seatReader.getByBookableDateId(bookableDate.id ?: 0)
        return SeatServiceResponse.from(bookableDate, seats)
    }
}
