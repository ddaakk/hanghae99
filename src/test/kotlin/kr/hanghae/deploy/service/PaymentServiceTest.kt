package kr.hanghae.deploy.service

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kr.hanghae.deploy.component.BookingReader
import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.domain.*
import kr.hanghae.deploy.dto.payment.PayBookingServiceRequest
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class PaymentServiceTest : DescribeSpec({
    val userReader = mockk<UserReader>()
    val bookingReader = mockk<BookingReader>()
    val redisService = mockk<RedisService>()
    val paymentService = PaymentService(userReader, bookingReader, redisService)

    describe("payBooking") {
        context("예약 가능 날짜와 좌석 번호들을 가지고") {

            every { userReader.getByUUID(any()) } returns User(uuid = "uuid")
            every { bookingReader.getByBookingNumberWithLock(any(), any()) } returns Booking(
                seats = mutableListOf(),
                number = "1234",
                userId = 1,
                date = LocalDate.now(),
                status = BookingStatus.BOOKING
            )
            every { redisService.getValue(any()) } returns "1234"
            every { redisService.removeValue(any()) } just runs

            it("좌석 예약을 수행한다") {
                val payment = paymentService.payBooking(
                    PayBookingServiceRequest(
                        bookingNumber = "1234", uuid = "uuid"
                    )
                )

                payment.uuid shouldBe "uuid"
                payment.booking.seats shouldBe emptyList()
                payment.booking.number shouldBe "1234"
                payment.booking.date shouldBe LocalDate.now()
                payment.booking.status shouldBe BookingStatus.BOOKED
            }
        }
    }
})
