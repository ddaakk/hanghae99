package kr.hanghae.deploy.service

import kr.hanghae.deploy.component.*
import kr.hanghae.deploy.domain.Booking
import kr.hanghae.deploy.dto.booking.BookingServiceRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookingService(
    private val userReader: UserReader,
    private val bookableDateReader: BookableDateReader,
    private val seatReader: SeatReader,
    private val seatsValidator: SeatsValidator,
    private val bookingManager: BookingManager,
) {

    @Transactional
    fun requestBooking(request: BookingServiceRequest): Booking {
        val (date, seatNumbers, uuid) = request
        val user = userReader.getByUUID(uuid)
        val bookableDate = bookableDateReader.getByDate(date)
        val seats = seatReader.getByOrderAndDate(seatNumbers, date)

        seatsValidator.validate(seatNumbers, seats)

        return bookingManager.requestBooking(user, seats, bookableDate)
    }
}
