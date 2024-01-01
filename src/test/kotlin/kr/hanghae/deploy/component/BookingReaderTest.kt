package kr.hanghae.deploy.component

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Booking
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.repository.BookingRepository
import kr.hanghae.deploy.repository.UserRepository
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
class BookingReaderTest: DescribeSpec({

    val bookingRepository = mockk<BookingRepository>()
    val bookingReader = BookingReader(bookingRepository)

    describe("getByBookingNumber") {
        context("예약 번호와 사용자 UUID 정보가 주어지면") {

            every {
                bookingRepository.findByBookingNumber(any(), any())
            } returns Booking(userId = 1, date = LocalDate.now())

            it("해당 예약 정보를 반환한다") {
                val booking = bookingReader.getByBookingNumber(bookingNumber = "1234", userId = 1)
                booking.userId shouldBe 1
                booking.seats shouldBe emptyList()
                booking.date shouldBe LocalDate.now()
            }
        }
    }

    describe("getByBookingNumber 실패") {
        context("잘못된 예약 번호와 사용자 UUID 정보가 주어지면") {

            every { bookingRepository.findByBookingNumber(any(), any()) } returns null

            it("해당 예약 정보 반환에 실패한다") {
                shouldThrow<RuntimeException> {
                    bookingReader.getByBookingNumber(bookingNumber = "1234", userId = 1)
                }.message shouldBe "존재하지 않는 예약 번호입니다."
            }
        }
    }
})
