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
import kr.hanghae.deploy.repository.BookableDateRepository
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
class BookableDateReaderTest : DescribeSpec({

    val bookableDateRepository = mockk<BookableDateRepository>()
    val bookableDateReader = BookableDateReader(bookableDateRepository)


    describe("getByDate") {
        context("예약 가능한 날에 해당하는") {

            every {
                bookableDateRepository.findByDate(any())
            } returns BookableDate(date = LocalDate.now(), concert = Concert(name = "고척돔"))

            it("예약 가능 일자 정보를 조회한다") {
                val bookableDate = bookableDateReader.getByDate(LocalDate.now())
                bookableDate.date shouldBe LocalDate.now()
                bookableDate.concert.name shouldBe "고척돔"
            }
        }
    }

    describe("getByDate 실패") {
        context("예약 가능한 날에 해당하는") {

            every { bookableDateRepository.findByDate(any()) } returns null

            it("해당하는 예약 가능 일자가 존재하지 않는다") {
                shouldThrow<RuntimeException> {
                    bookableDateReader.getByDate(LocalDate.now())
                }.message shouldBe "예약할 수 없는 날짜입니다."
            }
        }
    }

    describe("getByConcertAndDate") {
        context("콘서트 번호와 예약 가능 일자를 통해") {

            every {
                bookableDateRepository.findByConcertAndDate(any(), any())
            } returns BookableDate(date = LocalDate.now(), concert = Concert(name = "고척돔", number = "1234"))

            it("예약 가능 일자 정보를 조회한다") {
                val bookableDate =
                    bookableDateReader.getByConcertAndDate(concertNumber = "1234", date = LocalDate.now())
                bookableDate.date shouldBe LocalDate.now()
                bookableDate.concert.name shouldBe "고척돔"
                bookableDate.concert.number shouldBe "1234"
            }
        }
    }

    describe("getByConcertAndDate 실패") {
        context("콘서트 번호와 예약 가능 일자를 통해 조회를 시도하지만") {

            every { bookableDateRepository.findByConcertAndDate(any(), any()) } returns null

            it("해당하는 예약 가능 일자 정보가 존재하지 않는다") {
                shouldThrow<RuntimeException> {
                    bookableDateReader.getByConcertAndDate(concertNumber = "1234", date = LocalDate.now())
                }.message shouldBe "예약할 수 없는 날짜입니다."
            }
        }
    }
})
