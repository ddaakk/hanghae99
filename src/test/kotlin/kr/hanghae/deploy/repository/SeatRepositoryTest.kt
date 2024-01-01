package kr.hanghae.deploy.repository

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe
import kr.hanghae.deploy.DatabaseCleanUpExecutor
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.txContext
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.jdbc.core.JdbcTemplate
import java.time.LocalDate

@DataJpaTest
internal class SeatRepositoryTest(
    private val jdbcTemplate: JdbcTemplate,
    private val seatRepositoryImpl: SeatRepositoryImpl,
    private val bookableDateRepositoryImpl: BookableDateRepositoryImpl,
    private val concertRepositoryImpl: ConcertRepositoryImpl,
) : DescribeSpec() {

    override suspend fun beforeEach(testCase: TestCase) {
        val concert = Concert(name = "고척돔")
        concertRepositoryImpl.save(concert)

        val bookableDate = BookableDate(date = LocalDate.now(), concert = concert)
        bookableDateRepositoryImpl.save(bookableDate)
        val seats = mutableListOf(
            Seat(bookableDateId = 1, number = 1, grade = "A"),
            Seat(bookableDateId = 1, number = 2, grade = "A"),
        )
        seatRepositoryImpl.saveAll(seats)
    }

    init {
        describe("findByBookableDateId") {
            txContext("예약 가능한 날짜 아이디가 주어지면") {
                it("해당 날짜의 좌석들이 반환된다") {
                    val seats = seatRepositoryImpl.findByBookableDateId(bookableDateId = 1)
                    seats.size shouldBe 2
                    seats[0].bookableDateId shouldBe 1
                    seats[0].number shouldBe 1
                    seats[0].grade shouldBe "A"
                    seats[1].bookableDateId shouldBe 1
                    seats[1].number shouldBe 2
                    seats[1].grade shouldBe "A"
                }
            }
        }

        describe("findByOrderAndDate") {
            txContext("좌석 순서의 번호들과 예약 날짜 아이디가 주어지면") {
                it("해당 날짜의 좌석들이 반환된다") {
                    val seats = seatRepositoryImpl.findByOrderAndDate(seatNumbers = listOf(1, 2), bookableDateId = 1)
                    seats.size shouldBe 2
                    seats[0].bookableDateId shouldBe 1
                    seats[0].number shouldBe 1
                    seats[0].grade shouldBe "A"
                    seats[1].bookableDateId shouldBe 1
                    seats[1].number shouldBe 2
                    seats[1].grade shouldBe "A"
                }
            }
        }
    }

    override fun listeners(): List<TestListener> = listOf(DatabaseCleanUpExecutor(jdbcTemplate))
}
