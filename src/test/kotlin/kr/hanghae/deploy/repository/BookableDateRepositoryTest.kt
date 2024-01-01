package kr.hanghae.deploy.repository

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe
import kr.hanghae.deploy.DatabaseCleanUpExecutor
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.txContext
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.jdbc.core.JdbcTemplate
import java.time.LocalDate

@DataJpaTest
internal class BookableDateRepositoryTest(
    private val jdbcTemplate: JdbcTemplate,
    private val bookableDateRepositoryImpl: BookableDateRepositoryImpl,
    private val concertRepositoryImpl: ConcertRepositoryImpl,
) : DescribeSpec() {

    override suspend fun beforeEach(testCase: TestCase) {
        val concert = Concert(name = "고척돔", number = "1234")
        concertRepositoryImpl.save(concert)
        val firstBookableDate = BookableDate(date = LocalDate.now(), concert = concert)
        val secondBookableDate = BookableDate(date = LocalDate.now().plusDays(1), concert = concert)
        bookableDateRepositoryImpl.saveAll(listOf(firstBookableDate, secondBookableDate))
    }

    init {
        describe("findByDate") {
            txContext("예약 가능한 날짜 정보가 주어지면") {
                it("해당 날짜 정보가 반환된다") {
                    val bookableDate = bookableDateRepositoryImpl.findByDate(LocalDate.now())
                    bookableDate!!.date shouldBe LocalDate.now()
                }
            }
        }

        describe("findByConcertAndDate") {
            txContext("콘서트 번호와 날짜가 주어지면") {
                it("해당 날짜 정보가 반환된다") {
                    val bookableDate =
                        bookableDateRepositoryImpl.findByConcertAndDate(concertNumber = "1234", date = LocalDate.now())
                    bookableDate!!.date shouldBe LocalDate.now()
                    bookableDate.concert.name shouldBe "고척돔"
                    bookableDate.concert.number shouldBe "1234"
                }
            }
        }
    }

    override fun listeners(): List<TestListener> = listOf(DatabaseCleanUpExecutor(jdbcTemplate))
}
