package kr.hanghae.deploy.service

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.just
import io.mockk.runs
import kr.hanghae.deploy.component.BookingReader
import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.domain.*
import kr.hanghae.deploy.dto.payment.PayBookingServiceRequest
import kr.hanghae.deploy.repository.PaymentRepository
import kr.hanghae.deploy.repository.PaymentRepositoryImpl
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class PaymentServiceTest : DescribeSpec({
    val paymentRepositoryImpl = mockk<PaymentRepositoryImpl>()
    val userReader = mockk<UserReader>()
    val bookingReader = mockk<BookingReader>()
    val paymentService = PaymentService(paymentRepositoryImpl, userReader, bookingReader)

    describe("payBooking") {
        context("예약 가능 날짜와 좌석 번호들을 가지고") {

            val user = User(uuid = "uuid")
            val bookableDate = BookableDate(date = "2023-12-01")
            val booking = Booking(user, seats = mutableListOf(), bookableDate, number = "1234")
            val payment = Payment(booking, status = PayStatus.PAY_COMPLETE)

            val serviceRequest = PayBookingServiceRequest("1234", "uuid")

            every { paymentRepositoryImpl.saveAndFlush(any()) } returns payment
            every { userReader.getByUUID(any()) } returns user
            every { bookingReader.getByBookingNumber(any(), any()) } returns booking

            it("좌석 예약을 수행한다") {
                val payment = paymentService.payBooking(serviceRequest)
                payment.booking.user.uuid shouldBe "uuid"
                payment.booking.bookableDate!!.date shouldBe "2023-12-01"
                payment.booking.number shouldBe "1234"
                payment.booking.seats shouldBe emptyList()
                payment.booking.status shouldBe BookingStatus.RESERVED
                payment.status shouldBe PayStatus.PAY_COMPLETE
            }
        }
    }
})
