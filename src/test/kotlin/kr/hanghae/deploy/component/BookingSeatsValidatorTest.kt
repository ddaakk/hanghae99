package kr.hanghae.deploy.component

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.repository.BookableDateRepository
import kr.hanghae.deploy.repository.BookingSeatRepository
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BookingSeatsValidatorTest : DescribeSpec({

    val bookingSeatRepository = mockk<BookingSeatRepository>()
    val bookingSeatsValidator = BookingSeatsValidator(bookingSeatRepository)

    describe("validateSeatsBooking 이미 예약된 좌석이 존재하는 경우 실패") {
        context("이미 예약된 좌석이 존재하는 경우") {

            every { bookingSeatRepository.countBySeatIds(any()) } returns 1

            it("좌석 예약에 실패한다") {
                shouldThrow<RuntimeException> {
                    bookingSeatsValidator.validateSeatsBooking(listOf(1, 2))
                }.message shouldBe "이미 예약중인 좌석이 존재합니다."
            }
        }
    }
})
