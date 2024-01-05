package kr.hanghae.deploy.service

import kr.hanghae.deploy.annotation.DistributedLock
import kr.hanghae.deploy.component.*
import kr.hanghae.deploy.domain.Booking
import kr.hanghae.deploy.dto.booking.BookingServiceRequest
import kr.hanghae.deploy.dto.booking.BookingServiceResponse
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

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

        logger.info {
            "좌석 예약에 성공하였습니다. 콘서트 번호: $concertNumber, " +
                "날짜: $date, " +
                "좌석 번호: $seatNumbers, " +
                "사용자 아이디: $uuid"
        }

        return BookingServiceResponse.from(uuid, booking)
    }
}
