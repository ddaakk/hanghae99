package kr.hanghae.deploy.service

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.component.BookableDateReader
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.dto.bookabledate.response.BookableDateResponse
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BookableDateServiceTest: DescribeSpec({

    val bookableDateReader = mockk<BookableDateReader>()
    val bookableDateService = BookableDateService(bookableDateReader)

    describe("getBookableDates") {
        context("예약 가능한 날들을 등록하고") {

            val firstBookableDate = BookableDate(date = "2023-12-01")
            val secondBookableDate = BookableDate(date = "2023-12-02")
            val bookableDates = listOf(firstBookableDate, secondBookableDate)

            every { bookableDateReader.reader() } returns bookableDates

            val bookableDateResponses = listOf(
                BookableDateResponse.of(bookableDate = firstBookableDate),
                BookableDateResponse.of(bookableDate = secondBookableDate)
            )

            it("전체 예약 가능 일자들을 조회한다") {
                val response = bookableDateService.getBookableDates()
                response shouldHaveSize 2
                response shouldBe bookableDateResponses
            }
        }
    }
})
