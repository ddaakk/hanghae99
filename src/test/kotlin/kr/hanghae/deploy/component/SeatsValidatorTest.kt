package kr.hanghae.deploy.component

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SeatsValidatorTest(private val seatsValidator: SeatsValidator) : DescribeSpec({

    describe("validate 잘못된 번호의 좌석이 포함된 경우 실패") {
        context("잘못된 좌석 번호가 포함된 경우") {

            val bookableDate = BookableDate(date = "2023-12-01")
            val concert = Concert("고척돔")
            val firstSeat = Seat(bookableDate = bookableDate, number = 1, concert = concert, grade = "A")
            val secondSeat = Seat(bookableDate = bookableDate, number = 2, concert = concert, grade = "A")
            val seats = mutableListOf(firstSeat, secondSeat)
            val seatNumbers = listOf(1, 3)

            it("좌석 검증에 실패한다") {
                shouldThrow<RuntimeException> {
                    seatsValidator.validate(seatNumbers = seatNumbers, seats = seats)
                }.message shouldBe "잘못된 번호의 좌석이 존재합니다."
            }
        }
    }
})
