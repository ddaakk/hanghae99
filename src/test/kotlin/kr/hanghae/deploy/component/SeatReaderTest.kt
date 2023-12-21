package kr.hanghae.deploy.component

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.domain.bookabledate.BookableDate
import kr.hanghae.deploy.domain.seat.Seat
import kr.hanghae.deploy.domain.seat.SeatRepository
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SeatReaderTest: DescribeSpec({

    val seatRepository = mockk<SeatRepository>()
    val seatReader = SeatReader(seatRepository)

    describe("readerByDate") {
        context("예약 가능한 날에 해당하는") {
            val bookableDate = BookableDate(date = "2023-12-01")
            val firstSeat = Seat(bookableDate = bookableDate, orders = 1)
            val secondSeat = Seat(bookableDate = bookableDate, orders = 2)

            val seats = listOf(firstSeat, secondSeat)

            every { seatRepository.findAllByDate("2023-12-01") } returns seats

            it("좌석들을 조회한다") {
                val response = seatReader.readerByDate("2023-12-01")
                response shouldHaveSize 2
                response shouldBe seats
            }
        }
    }


    describe("readerByDate fail") {
        context("예약 가능한 날에 해당하는") {

            every { seatRepository.findAllByDate("2023-12-01") } returns listOf()

            it("좌석들이 존재하지 않는다") {
                shouldThrow<RuntimeException> {
                    seatReader.readerByDate("2023-12-01")
                }.message shouldBe "예약할 좌석이 존재하지 않습니다."
            }
        }
    }

    describe("readerByDateAndOrder") {
        context("예약 가능 날짜 정보와 좌석 순번을 통해") {
            val bookableDate = BookableDate(date = "2023-12-01")
            val firstSeat = Seat(bookableDate = bookableDate, orders = 1)
            val secondSeat = Seat(bookableDate = bookableDate, orders = 2)

            val seats = mutableListOf(firstSeat, secondSeat)

            every { seatRepository.findByOrderAndDate(listOf(1, 2), "2023-12-01") } returns seats

            it("해당하는 좌석들을 조회한다") {
                val response = seatReader.readerByOrderAndDate(seatOrders = listOf(1, 2), date = "2023-12-01")
                response shouldHaveSize 2
                response shouldBe seats
                seats[0].orders shouldBe 1
                seats[1].orders shouldBe 2
            }
        }
    }

    describe("readerByDateAndOrder Fail") {
        context("예약 가능 날짜 정보와 좌석 순번이 주어지지만") {

            every { seatRepository.findByOrderAndDate(listOf(1, 2), "2023-12-01") } returns mutableListOf()

            it("해당하는 좌석들이 없어 조회에 실패한다") {
                shouldThrow<RuntimeException> {
                    seatReader.readerByOrderAndDate(seatOrders = listOf(1, 2), date = "2023-12-01")
                }.message shouldBe "예약할 좌석이 존재하지 않습니다."
            }
        }
    }
})
