package kr.hanghae.deploy.repository

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe
import kr.hanghae.deploy.DatabaseCleanUpExecutor
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.txContext
import kr.hanghae.deploy.repository.UserRepositoryImpl
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@DataJpaTest
internal class ConcertRepositoryTest(
    private val jdbcTemplate: JdbcTemplate,
    private val concertRepositoryImpl: ConcertRepositoryImpl,
    private val bookableDateRepositoryImpl: BookableDateRepositoryImpl,
) : DescribeSpec() {

    override suspend fun beforeEach(testCase: TestCase) {
        val concert = Concert(number = "1234", name = "고척돔")
        val firstBookableDate = BookableDate(date = LocalDate.now(), concert = concert)
        val secondBookableDate = BookableDate(date = LocalDate.now().plusDays(1), concert = concert)
        concert.updateBookableDates(mutableListOf(firstBookableDate, secondBookableDate))
        concertRepositoryImpl.save(concert)
        bookableDateRepositoryImpl.saveAll(listOf(firstBookableDate, secondBookableDate))
    }

    init {
        describe("findByConcertNumber") {
            txContext("콘서트 번호가 주어지면") {
                it("해당 콘서트의 정보를 반환한다") {
                    val concert = concertRepositoryImpl.findByConcertNumber("1234")
                    concert!!.number shouldBe "1234"
                    concert.name shouldBe "고척돔"
                }
            }
        }

        describe("findByConcertNumberAndDate") {
            txContext("콘서트 번호와 예약 가능 날짜가 주어지면") {
                it("해당 콘서트 정보가 반환된다") {
                    val concert = concertRepositoryImpl.findByConcertNumberAndDate(
                        concertNumber = "1234",
                        date = LocalDate.now()
                    )
                    concert!!.number shouldBe "1234"
                    concert.name shouldBe "고척돔"
                }
            }
        }
    }

    override fun listeners(): List<TestListener> = listOf(DatabaseCleanUpExecutor(jdbcTemplate))
}
