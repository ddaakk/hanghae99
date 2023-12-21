package kr.hanghae.deploy.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.component.BookableDateReader
import kr.hanghae.deploy.component.SeatReader
import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.domain.bookabledate.BookableDate
import kr.hanghae.deploy.domain.seat.Seat
import kr.hanghae.deploy.domain.seat.SeatStatus
import kr.hanghae.deploy.domain.user.User
import kr.hanghae.deploy.dto.booking.BookingServiceRequest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BookingServiceTest : DescribeSpec({

    val userReader = mockk<UserReader>()
    val bookableDateReader = mockk<BookableDateReader>()
    val seatReader = mockk<SeatReader>()
    val bookingService = BookingService(userReader, bookableDateReader, seatReader)

    describe("requestBooking") {
        context("예약 가능 날짜와 좌석 번호들을 가지고") {

            val user = User("uuid")
            val bookableDate = BookableDate(date = "2023-12-01")
            val firstSeat = Seat(bookableDate = bookableDate, orders = 1, price = 2000)
            val secondSeat = Seat(bookableDate = bookableDate, orders = 2, price = 4000)
            val serviceRequest = BookingServiceRequest(
                date = "2023-12-01",
                seatOrder = mutableListOf(1, 2),
                uuid = "uuid"
            )

            val seats = mutableListOf(firstSeat, secondSeat)

            every { userReader.readerByUuid(any<String>()) } returns user
            every { bookableDateReader.readerByDate(any<String>()) } returns bookableDate
            every {
                seatReader.readerByOrderAndDate(date = any<String>(), seatOrders = any<List<Long>>())
            } returns seats

            it("좌석 예약을 수행한다") {
                val booking = bookingService.requestBooking(serviceRequest)
                booking.uuid shouldBe "uuid"
                booking.date shouldBe "2023-12-01"
                booking.seatOrder shouldBe listOf(1, 2)
                booking.totalPrice shouldBe 6000
            }
        }
    }

    describe("requestBooking 잘못된 번호의 좌석이 포함된 요청 실패") {
        context("예약 가능 날짜와 잘못된 좌석 번호가 포함된 좌석 번호를 가지고") {

            val user = User("uuid")
            val bookableDate = BookableDate(date = "2023-12-01")
            val firstSeat = Seat(bookableDate = bookableDate, orders = 1)
            val secondSeat = Seat(bookableDate = bookableDate, orders = 2)
            val serviceRequest = BookingServiceRequest(
                date = "2023-12-01",
                seatOrder = mutableListOf(1, 3),
                uuid = "uuid"
            )

            val seats = mutableListOf(firstSeat, secondSeat)

            every { userReader.readerByUuid(any<String>()) } returns user
            every { bookableDateReader.readerByDate(any<String>()) } returns bookableDate
            every {
                seatReader.readerByOrderAndDate(date = any<String>(), seatOrders = any<List<Long>>())
            } returns seats

            it("좌석 예약에 실패한다") {
                shouldThrow<RuntimeException> {
                    bookingService.requestBooking(serviceRequest)
                }.message shouldBe "잘못된 번호의 좌석이 존재합니다."
            }
        }
    }

    describe("requestBooking 예약이 불가능한 좌석이 포함된 요청 실패") {
        context("예약 가능 날짜와 이미 예약 진행중인 예약 불가능 좌석 번호를 가지고") {

            val user = User("uuid")
            val bookableDate = BookableDate(date = "2023-12-01")
            val firstSeat = Seat(bookableDate = bookableDate, orders = 1, seatStatus = SeatStatus.BOOKED)
            val secondSeat = Seat(bookableDate = bookableDate, orders = 2)
            val serviceRequest = BookingServiceRequest(
                date = "2023-12-01",
                seatOrder = mutableListOf(1, 2),
                uuid = "uuid"
            )

            val seats = mutableListOf(firstSeat, secondSeat)

            every { userReader.readerByUuid(any<String>()) } returns user
            every { bookableDateReader.readerByDate(any<String>()) } returns bookableDate
            every {
                seatReader.readerByOrderAndDate(date = any<String>(), seatOrders = any<List<Long>>())
            } returns seats

            it("좌석 예약에 실패한다") {
                shouldThrow<RuntimeException> {
                    bookingService.requestBooking(serviceRequest)
                }.message shouldBe "예약이 불가능한 좌석이 존재합니다."
            }
        }
    }
})
