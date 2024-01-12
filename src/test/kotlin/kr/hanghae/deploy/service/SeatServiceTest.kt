package kr.hanghae.deploy.service

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.component.BookableDateReader
import kr.hanghae.deploy.component.SeatReader
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.dto.seat.SeatServiceRequest
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class SeatServiceTest : DescribeSpec({

    val seatReader = mockk<SeatReader>()
    val bookableDateReader = mockk<BookableDateReader>()
    val seatService = SeatService(seatReader, bookableDateReader)

    describe("getSeatsByDate") {
        context("예약 가능한 날에 해당하는") {

            val concert = Concert(name = "고척돔", number = "1234")
            val bookableDate = BookableDate(concert = concert, date = LocalDate.now())
            val firstSeat = Seat(bookableDateId = 1, number = 1, grade = "A")
            val secondSeat = Seat(bookableDateId = 1, number = 2, grade = "B")

            val seats = listOf(firstSeat, secondSeat)

            every { bookableDateReader.getByConcertAndDate(any(), any()) } returns bookableDate
            every { seatReader.getByBookableDateId(any()) } returns seats

            it("좌석들을 조회한다") {
                val response = seatService.getSeatsByConcertAndDate(
                    SeatServiceRequest.toService(
                        concertNumber = "1234",
                        date = LocalDate.now()
                    )
                )
                response.seats[0].number shouldBe 1
                response.seats[0].grade shouldBe "A"
                response.seats[1].number shouldBe 2
                response.seats[1].grade shouldBe "B"
                response.bookableDate.concert.name shouldBe "고척돔"
                response.bookableDate.date shouldBe LocalDate.now()
            }
        }
    }
})
