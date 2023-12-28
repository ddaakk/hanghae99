package kr.hanghae.deploy.service

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kr.hanghae.deploy.component.*
import kr.hanghae.deploy.domain.*
import kr.hanghae.deploy.dto.booking.BookingServiceRequest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BookingServiceTest : DescribeSpec({

    val userReader = mockk<UserReader>()
    val bookableDateReader = mockk<BookableDateReader>()
    val seatReader = mockk<SeatReader>()
    val seatsValidator = mockk<SeatsValidator>()
    val bookingManager = mockk<BookingManager>()
    val bookingService = BookingService(userReader, bookableDateReader, seatReader, seatsValidator, bookingManager)

    describe("requestBooking") {
        context("예약 가능 날짜와 좌석 번호들을 가지고") {

            val user = User(uuid = "uuid")
            val concert = Concert(name = "고척돔")
            val bookableDate = BookableDate(date = "2023-12-01")
            val firstSeat = Seat(bookableDate = bookableDate, number = 1, concert = concert, price = 2000, grade = "A")
            val secondSeat = Seat(bookableDate = bookableDate, number = 2, concert = concert, price = 4000, grade = "A")
            val serviceRequest = BookingServiceRequest(
                date = "2023-12-01",
                seatNumbers = mutableListOf(1, 2),
                uuid = "uuid"
            )

            val seats = mutableListOf(firstSeat, secondSeat)

            every { userReader.getByUUID(any()) } returns user
            every { bookableDateReader.getByDate(any()) } returns bookableDate
            every {
                seatReader.getByOrderAndDate(date = any(), seatNumbers = any())
            } returns seats
            every { seatsValidator.validate(any(), any()) } just runs
            every {
                bookingManager.requestBooking(any(), any(), any())
            } returns Booking(user, seats, bookableDate)

            it("좌석 예약을 수행한다") {
                val booking = bookingService.requestBooking(serviceRequest)
                booking.user.uuid shouldBe "uuid"
                booking.seats shouldBe listOf(firstSeat, secondSeat)
                booking.bookableDate!!.date shouldBe "2023-12-01"
            }
        }
    }
})
