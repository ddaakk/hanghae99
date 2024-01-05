package kr.hanghae.deploy.service

import kr.hanghae.deploy.component.BookingReader
import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.domain.BookingStatus
import kr.hanghae.deploy.dto.payment.PayBookingServiceRequest
import kr.hanghae.deploy.dto.payment.PayBookingServiceResponse
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
class PaymentService (
    private val userReader: UserReader,
    private val bookingReader: BookingReader,
){
    @Transactional
    fun payBooking(request: PayBookingServiceRequest): PayBookingServiceResponse {
        val (bookingNumber, uuid) = request

        val user = userReader.getByUUID(uuid);
        val booking = bookingReader.getByBookingNumber(bookingNumber, userId = user.id ?: 0)

        if (booking.status == BookingStatus.BOOKED) {
            throw RuntimeException("이미 구매가 완료된 예약입니다.")
        }

        user.payBooking(booking.getTotalPrice())
        booking.changeToBooked()

        logger.info {
            "좌석 구매에 성공하였습니다. 예약 번호: $bookingNumber, " +
                "사용자 아이디: $uuid"
        }

        return PayBookingServiceResponse.from(uuid, booking)
    }
}
