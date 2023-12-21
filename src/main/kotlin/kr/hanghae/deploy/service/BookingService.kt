package kr.hanghae.deploy.service

import kr.hanghae.deploy.component.BookableDateReader
import kr.hanghae.deploy.component.SeatReader
import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.domain.booking.Booking
import kr.hanghae.deploy.domain.payment.Payment
import kr.hanghae.deploy.domain.seat.SeatStatus
import kr.hanghae.deploy.dto.booking.BookingServiceRequest
import kr.hanghae.deploy.dto.booking.response.BookingResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.RuntimeException
import java.util.*

@Service
@Transactional(readOnly = true)
class BookingService(
    private val userReader: UserReader,
    private val bookableDateReader: BookableDateReader,
    private val seatReader: SeatReader,
) {

    @Transactional
    fun requestBooking(request: BookingServiceRequest): BookingResponse {
        val (uuid, seatOrders, date) = request
        val user = userReader.readerByUuid(uuid)
        val bookableDate = bookableDateReader.readerByDate(date)
        val seats = seatReader.readerByOrderAndDate(seatOrders, date)

        if (!seats.all { seat -> seatOrders.contains(seat.orders) }) {
            throw RuntimeException("잘못된 번호의 좌석이 존재합니다.")
        }

        if (!seats.all { seat -> seat.seatStatus == SeatStatus.BOOKABLE }) {
            throw RuntimeException("예약이 불가능한 좌석이 존재합니다.")
        }

        val booking = Booking(user, seats, bookableDate, number = UUID.randomUUID().mostSignificantBits.toString())
        val payment = Payment(booking, seats = seats, user = user, totalPrice = seats.sumOf { it.price })
        booking.updatePayment(payment)

        seats.map { seat -> seat.changeToBooking() }

        return BookingResponse.of(booking)
    }
}
