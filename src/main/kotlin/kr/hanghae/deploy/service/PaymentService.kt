package kr.hanghae.deploy.service

import kr.hanghae.deploy.component.BookingReader
import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.domain.PayStatus
import kr.hanghae.deploy.domain.Payment
import kr.hanghae.deploy.dto.payment.PayBookingServiceRequest
import kr.hanghae.deploy.repository.PaymentRepository
import kr.hanghae.deploy.repository.PaymentRepositoryImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService (
    private val paymentRepositoryImpl: PaymentRepositoryImpl,
    private val userReader: UserReader,
    private val bookingReader: BookingReader,
){
    @Transactional
    fun payBooking(request: PayBookingServiceRequest): Payment {
        val (bookingNumber, uuid) = request

        val user = userReader.getByUUID(uuid);
        val booking = bookingReader.getByBookingNumber(bookingNumber, uuid)

        user.payBookingSeats(totalPrice = booking.getTotalPrice())
        val payment = Payment(booking, status = PayStatus.PAY_COMPLETE)
        val savedPayment = paymentRepositoryImpl.saveAndFlush(payment)
        booking.updatePayment(savedPayment)

        return savedPayment
    }
}
