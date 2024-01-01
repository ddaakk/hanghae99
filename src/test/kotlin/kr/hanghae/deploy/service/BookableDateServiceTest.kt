package kr.hanghae.deploy.service

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.component.ConcertReader
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.dto.bookabledate.BookableDateServiceRequest
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class BookableDateServiceTest: DescribeSpec({

    val concertReader = mockk<ConcertReader>()
    val bookableDateService = BookableDateService(concertReader)

    describe("getBookableDates") {
        context("예약 가능한 날들을 등록하고") {

            val concert = Concert(name = "고척돔", number = "1234")
            val firstBookableDate = BookableDate(concert = concert, date = LocalDate.now())
            val secondBookableDate = BookableDate(concert = concert, date = LocalDate.now())
            val bookableDates = mutableListOf(firstBookableDate, secondBookableDate)
            concert.updateBookableDates(bookableDates)

            every { concertReader.getByConcertNumber(any()) } returns concert

            it("전체 예약 가능 일자들을 조회한다") {
                val response = bookableDateService.getBookableDates(BookableDateServiceRequest("1234"))
                response shouldHaveSize 2
                response[0].date shouldBe LocalDate.now()
                response[1].date shouldBe LocalDate.now()
            }
        }
    }
})
