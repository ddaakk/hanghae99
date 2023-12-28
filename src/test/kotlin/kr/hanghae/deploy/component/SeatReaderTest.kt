package kr.hanghae.deploy.component

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.repository.SeatRepository
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SeatReaderTest: DescribeSpec({

    val seatRepository = mockk<SeatRepository>()
    val seatReader = SeatReader(seatRepository)

    describe("getByDate") {
        context("예약 가능한 날에 해당하는") {
            val bookableDate = BookableDate(date = "2023-12-01")
            val concert = Concert(name = "고척돔")
            val firstSeat = Seat(bookableDate = bookableDate, number = 1, concert = concert, grade = "A")
            val secondSeat = Seat(bookableDate = bookableDate, number = 2, concert = concert, grade = "A")

            val seats = listOf(firstSeat, secondSeat)

            every { seatRepository.findAllByDate("2023-12-01") } returns seats

            it("좌석들을 조회한다") {
                val response = seatReader.getByDate("2023-12-01")
                response shouldHaveSize 2
                response shouldBe seats
            }
        }
    }


    describe("getByDate 실패") {
        context("예약 가능한 날에 해당하는") {

            every { seatRepository.findAllByDate("2023-12-01") } returns listOf()

            it("좌석들이 존재하지 않는다") {
                shouldThrow<RuntimeException> {
                    seatReader.getByDate("2023-12-01")
                }.message shouldBe "예약할 좌석이 존재하지 않습니다."
            }
        }
    }

    describe("getByDateAndOrder") {
        context("예약 가능 날짜 정보와 좌석 순번을 통해") {
            val bookableDate = BookableDate(date = "2023-12-01")
            val concert = Concert("고척돔")
            val firstSeat = Seat(bookableDate = bookableDate, number = 1, concert = concert, grade = "A")
            val secondSeat = Seat(bookableDate = bookableDate, number = 2, concert = concert, grade = "A")

            val seats = mutableListOf(firstSeat, secondSeat)

            every { seatRepository.findByOrderAndDate(listOf(1, 2), "2023-12-01") } returns seats

            it("해당하는 좌석들을 조회한다") {
                val response = seatReader.getByOrderAndDate(seatNumbers = listOf(1, 2), date = "2023-12-01")
                response shouldHaveSize 2
                response shouldBe seats
                seats[0].number shouldBe 1
                seats[1].number shouldBe 2
            }
        }
    }

    describe("getByDateAndOrder 실패") {
        context("예약 가능 날짜 정보와 좌석 순번이 주어지지만") {

            every { seatRepository.findByOrderAndDate(listOf(1, 2), "2023-12-01") } returns mutableListOf()

            it("해당하는 좌석들이 없어 조회에 실패한다") {
                shouldThrow<RuntimeException> {
                    seatReader.getByOrderAndDate(seatNumbers = listOf(1, 2), date = "2023-12-01")
                }.message shouldBe "예약할 좌석이 존재하지 않습니다."
            }
        }
    }
})
