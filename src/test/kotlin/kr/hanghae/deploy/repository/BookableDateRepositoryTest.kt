package kr.hanghae.deploy.repository

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.txContext
import kr.hanghae.deploy.repository.BookableDateRepositoryImpl
import kr.hanghae.deploy.repository.ConcertRepositoryImpl
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
internal class BookableDateRepositoryTest(
    private val bookableDateRepositoryImpl: BookableDateRepositoryImpl,
    private val concertRepositoryImpl: ConcertRepositoryImpl,
) : DescribeSpec({

    describe("findByDateWithSeats") {
        txContext("예약 가능한 날짜 정보가 주어지면") {

            val concert = Concert("고척돔")
            concertRepositoryImpl.saveAndFlush(concert)

            val firstBookableDate = BookableDate(date = "2023-12-01")
            val secondBookableDate = BookableDate(date = "2023-12-02")
            val secondDateSeats = mutableListOf(
                Seat(bookableDate = secondBookableDate, number = 1, concert = concert, grade = "A"),
                Seat(bookableDate = secondBookableDate, number = 2, concert = concert, grade = "A"),
            )
            secondBookableDate.updateSeats(seats = secondDateSeats)
            bookableDateRepositoryImpl.saveAllAndFlush(listOf(firstBookableDate, secondBookableDate))

            it("좌석 정보들을 포함한 날짜 정보가 반환된다") {
                val bookableDates = bookableDateRepositoryImpl.findByDate()
                // verify
                bookableDates shouldHaveSize 2
                bookableDates[0].date shouldBe "2023-12-01"
                bookableDates[1].date shouldBe "2023-12-02"
                bookableDates[0].seats shouldHaveSize 0
                bookableDates[1].seats shouldHaveSize 2
                bookableDates[1].seats.map { it.number }.shouldContainExactlyInAnyOrder(listOf(1, 2))
            }
        }
    }

    describe("findByDate") {
        txContext("예약 가능한 날짜 정보가 주어지면") {
            val firstBookableDate = BookableDate(date = "2023-12-01")
            val secondBookableDate = BookableDate(date = "2023-12-02")
            bookableDateRepositoryImpl.saveAllAndFlush(listOf(firstBookableDate, secondBookableDate))

            it("해당 날짜 정보가 반환된다") {
                val bookableDate = bookableDateRepositoryImpl.findByDate("2023-12-02")
                bookableDate!!.date shouldBe "2023-12-02"
            }
        }
    }

    describe("findByDate 실패") {
        txContext("예약 가능한 날짜 정보가 주어지면") {
            val firstBookableDate = BookableDate(date = "2023-12-01")
            val secondBookableDate = BookableDate(date = "2023-12-02")
            bookableDateRepositoryImpl.saveAllAndFlush(listOf(firstBookableDate, secondBookableDate))

            it("해당 예약 날짜는 존재하지 않으므로 실패한다") {
                val bookableDate = bookableDateRepositoryImpl.findByDate("2023-12-03")
                bookableDate?.date shouldBe null
            }
        }
    }
})
