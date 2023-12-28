package kr.hanghae.deploy.component

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.repository.BookableDateRepository
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BookableDateReaderTest : DescribeSpec({

    val bookableDateRepository = mockk<BookableDateRepository>()
    val bookableDateReader = BookableDateReader(bookableDateRepository)

    describe("reader") {
        context("예약 가능한 날들을 등록하고") {

            val firstBookableDate = BookableDate(date = "2023-12-01")
            val secondBookableDate = BookableDate(date = "2023-12-02")
            val bookableDates = listOf(firstBookableDate, secondBookableDate)

            every { bookableDateRepository.findByDate() } returns bookableDates

            it("전체 예약 가능 일자들을 조회한다") {
                val response = bookableDateReader.reader()
                response shouldHaveSize 2
                response shouldBe bookableDates
            }
        }
    }

    describe("reader 실패") {
        context("전체 예약 가능 날짜 조회를 시도한다") {

            every { bookableDateRepository.findByDate() } returns listOf()

            it("예약 가능한 날짜가 존재하지 않아 실패한다") {
                shouldThrow<RuntimeException> {
                    bookableDateReader.reader()
                }.message shouldBe "예약 가능한 날이 존재하지 않습니다."
            }
        }
    }

    describe("getByDate") {
        context("예약 가능한 날에 해당하는") {

            val firstBookableDate = BookableDate(date = "2023-12-01")

            every { bookableDateRepository.findByDate("2023-12-01") } returns firstBookableDate

            it("예약 가능 일자 정보를 조회한다") {
                val bookableDate = bookableDateReader.getByDate("2023-12-01")
                bookableDate.date shouldBe "2023-12-01"
                bookableDate.seats shouldBe emptyList()
            }
        }
    }

    describe("getByDate 살패") {
        context("예약 가능한 날에 해당하는") {

            every { bookableDateRepository.findByDate("2023-12-01") } returns null

            it("해당하는 예약 가능 일자가 존재하지 않는다") {
                shouldThrow<RuntimeException> {
                    bookableDateReader.getByDate("2023-12-01")
                }.message shouldBe "예약할 수 없는 날짜입니다."
            }
        }
    }
})
