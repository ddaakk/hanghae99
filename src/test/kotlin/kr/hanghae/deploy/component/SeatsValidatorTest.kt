package kr.hanghae.deploy.component

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Booking
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

class SeatsValidatorTest : DescribeSpec({

    val seatsValidator = SeatsValidator()

    describe("validate 잘못된 번호의 좌석이 포함된 경우 실패") {
        context("잘못된 좌석 번호가 포함된 경우") {
            it("좌석 검증에 실패한다") {
                shouldThrow<RuntimeException> {
                    seatsValidator.validate(
                        seatNumbers = listOf(1, 3), seats = mutableListOf(
                            Seat(bookableDateId = 1, number = 1, grade = "A"),
                            Seat(bookableDateId = 1, number = 2, grade = "B")
                        )
                    )
                }.message shouldBe "잘못된 번호의 좌석이 존재합니다."
            }
        }
    }

    describe("validate 이미 예약 중인 좌석이 존재하는 경우 실패") {
        context("이미 예약 중인 좌석이 포함된 경우") {
            it("좌석 검증에 실패한다") {
                shouldThrow<RuntimeException> {
                    val booking = Booking(userId = 1, date = LocalDate.now())
                    seatsValidator.validate(
                        seatNumbers = listOf(1, 2), seats = mutableListOf(
                            Seat(bookableDateId = 1, number = 1, grade = "A", booking = booking),
                            Seat(bookableDateId = 1, number = 2, grade = "B")
                        )
                    )
                }.message shouldBe "이미 예약중인 좌석이 존재합니다."
            }
        }
    }
})
