package kr.hanghae.deploy.service

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.component.SeatReader
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.dto.seat.SeatServiceRequest
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SeatServiceTest: DescribeSpec({

    val seatReader = mockk<SeatReader>()
    val seatService = SeatService(seatReader)

    describe("getSeatsByDate") {
        context("예약 가능한 날에 해당하는") {

            val concert = Concert(name = "고척돔")
            val bookableDate = BookableDate(date = "2023-12-01")
            val firstSeat = Seat(bookableDate = bookableDate, number = 1, concert = concert, grade = "A")
            val secondSeat = Seat(bookableDate = bookableDate, number = 2, concert = concert, grade = "A")
            val serviceRequest = SeatServiceRequest(date = "2023-12-01")

            val seats = listOf(firstSeat, secondSeat)

            every { seatReader.getByDate(date = "2023-12-01") } returns seats

            it("좌석들을 조회한다") {
                val response = seatService.getSeatsByDate(serviceRequest)
                response shouldHaveSize 2
                response shouldBe seats
            }
        }
    }
})
