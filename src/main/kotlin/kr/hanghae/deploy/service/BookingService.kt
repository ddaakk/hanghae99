package kr.hanghae.deploy.service

import kr.hanghae.deploy.annotation.DistributedLock
import kr.hanghae.deploy.component.*
import kr.hanghae.deploy.domain.Booking
import kr.hanghae.deploy.dto.booking.BookingServiceRequest
import kr.hanghae.deploy.dto.booking.BookingServiceResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookingService(
    private val userReader: UserReader,
    private val seatReader: SeatReader,
    private val seatsValidator: SeatsValidator,
    private val bookingManager: BookingManager,
    private val concertReader: ConcertReader,
) {

    @Transactional
    @DistributedLock(key = "T(java.lang.String).format('booking%s', #request.concertNumber)")
    fun requestBooking(request: BookingServiceRequest): BookingServiceResponse {
        val (concertNumber, date, seatNumbers, uuid) = request
        val concert = concertReader.getByConcertNumberAndDate(concertNumber, date)
        val bookableDateId = concert.bookableDates[0].id ?: 0
        val user = userReader.getByUUID(uuid)
        val seats = seatReader.getByOrderAndDate(seatNumbers, bookableDateId)

        seatsValidator.validate(seatNumbers, seats)

        val booking = bookingManager.requestBooking(user, seats, concert.bookableDates[0])

        return BookingServiceResponse.from(uuid, booking)
    }
}
