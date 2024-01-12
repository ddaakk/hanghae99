package kr.hanghae.deploy.component

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.domain.*
import kr.hanghae.deploy.repository.*
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
class BookingManagerTest : DescribeSpec({

    val bookingRepositoryImpl = mockk<BookingRepositoryImpl>()
    val bookingManager = BookingManager(bookingRepositoryImpl)

    describe("requestBooking") {
        context("예약 가능한 모든 콘서트에 대한") {

            val seats = mutableListOf(
                Seat(bookableDateId = 1, number = 1, grade = "A"),
                Seat(bookableDateId = 1, number = 2, grade = "B"),
            )

            every {
                bookingRepositoryImpl.save(any())
            } returns Booking(userId = 1, date = LocalDate.now(), seats = seats, number = "1234")

            it("콘서트 정보들을 반환한다") {
                val booking = bookingManager.requestBooking(
                    user = User(uuid = "uuid"),
                    seats = seats,
                    BookableDate(date = LocalDate.now(), Concert(name = "고척돔", number = "5678"))
                )

                booking.userId shouldBe 1
                booking.date shouldBe LocalDate.now()
                booking.status shouldBe BookingStatus.BOOKING
                booking.seats.size shouldBe 2
            }
        }
    }
})
