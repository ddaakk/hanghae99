package kr.hanghae.deploy.service

import kr.hanghae.deploy.domain.booking.BookingRepository
import kr.hanghae.deploy.domain.payment.PayStatus
import kr.hanghae.deploy.domain.payment.Payment
import kr.hanghae.deploy.domain.payment.PaymentRepository
import kr.hanghae.deploy.dto.payment.request.PaymentRequest
import kr.hanghae.deploy.dto.payment.response.PaymentResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class PaymentService (
    private val paymentRepository: PaymentRepository,
){
    @Transactional
    fun payBooking(request: PaymentRequest, uuid: String): PaymentResponse {
        val payment = paymentRepository.findByBookingNumber(uuid, request.bookingNumber)

        payment ?: throw IllegalArgumentException("존재하지 않는 예약입니다.")

        payment.payStatus = PayStatus.PAY_COMPLETE

        return PaymentResponse.of(payment)
    }


}
