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

    describe("getByBookableDateId") {
        context("예약 가능 날짜의 아이디에 해당하는") {
            val firstSeat = Seat(bookableDateId = 1, number = 1, grade = "A")
            val secondSeat = Seat(bookableDateId = 1, number = 2, grade = "B")

            every { seatRepository.findByBookableDateId(any()) } returns listOf(firstSeat, secondSeat)

            it("좌석들을 조회한다") {
                val seats = seatReader.getByBookableDateId(bookableDateId = 1)
                seats shouldHaveSize 2
                seats[0].bookableDateId shouldBe 1
                seats[0].number shouldBe 1
                seats[0].grade shouldBe "A"
                seats[1].bookableDateId shouldBe 1
                seats[1].number shouldBe 2
                seats[1].grade shouldBe "B"
            }
        }
    }


    describe("getByBookableDateId 실패") {
        context("예약 가능 날짜의 아이디에 해당하는 좌석들을 조회를 시도하지만") {

            every { seatRepository.findByBookableDateId(any()) } returns listOf()

            it("좌석들이 존재하지 않는다") {
                shouldThrow<RuntimeException> {
                    seatReader.getByBookableDateId(bookableDateId = 1)
                }.message shouldBe "예약할 좌석이 존재하지 않습니다."
            }
        }
    }

    describe("getByOrderAndDate") {
        context("좌석 순번과 예약 가능 날짜 정보에") {
            val firstSeat = Seat(bookableDateId = 1, number = 1, grade = "A")
            val secondSeat = Seat(bookableDateId = 1, number = 2, grade = "B")

            every { seatRepository.findByOrderAndDate(any(), any()) } returns mutableListOf(firstSeat, secondSeat)

            it("해당하는 좌석들을 조회한다") {
                val seats = seatReader.getByOrderAndDate(seatNumbers = listOf(1, 2), bookableDateId = 1)
                seats shouldHaveSize 2
                seats[0].bookableDateId shouldBe 1
                seats[0].number shouldBe 1
                seats[0].grade shouldBe "A"
                seats[1].bookableDateId shouldBe 1
                seats[1].number shouldBe 2
                seats[1].grade shouldBe "B"
            }
        }
    }

    describe("getByOrderAndDate 실패") {
        context("좌석 순번과 예약 가능 날짜 정보를 통해 조회를 시도하지만") {

            every { seatRepository.findByOrderAndDate(any(), any()) } returns mutableListOf()

            it("해당하는 좌석들이 없어 조회에 실패한다") {
                shouldThrow<RuntimeException> {
                    seatReader.getByOrderAndDate(seatNumbers = listOf(1, 2), bookableDateId = 1)
                }.message shouldBe "예약할 좌석이 존재하지 않습니다."
            }
        }
    }
})
