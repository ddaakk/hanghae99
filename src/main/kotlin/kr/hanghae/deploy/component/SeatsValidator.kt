package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.repository.BookingSeatRepository
import kr.hanghae.deploy.repository.BookingSeatRepositoryImpl
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class SeatsValidator(
    private val bookingSeatsValidator: BookingSeatsValidator,
) {
    fun validate(seatNumbers: List<Int>, seats: List<Seat>) {
        if (!seats.all { seat -> seatNumbers.contains(seat.number) }) {
            throw RuntimeException("잘못된 번호의 좌석이 존재합니다.")
        }

        bookingSeatsValidator.validateSeatsBooking(seats.map { it.id!! })
    }
}
