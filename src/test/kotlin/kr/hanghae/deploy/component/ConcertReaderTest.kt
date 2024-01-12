package kr.hanghae.deploy.component

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.domain.*
import kr.hanghae.deploy.repository.BookingRepository
import kr.hanghae.deploy.repository.ConcertRepository
import kr.hanghae.deploy.repository.ConcertRepositoryImpl
import kr.hanghae.deploy.repository.UserRepository
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
class ConcertReaderTest : DescribeSpec({

    val concertRepositoryImpl = mockk<ConcertRepositoryImpl>()
    val concertReader = ConcertReader(concertRepositoryImpl)

    describe("getAll") {
        context("예약 가능한 모든 콘서트에 대한") {

            every {
                concertRepositoryImpl.findAll()
            } returns listOf(Concert(name = "고척돔", number = "1234"), Concert(name = "아레나", number = "5678"))

            it("콘서트 정보들을 반환한다") {
                val concerts = concertReader.getAll()
                concerts.size shouldBe 2
                concerts[0].name shouldBe "고척돔"
                concerts[1].name shouldBe "아레나"
            }
        }
    }

    describe("getAll 실패") {
        context("예약 가능한 모든 콘서트 조회를 시도하지만") {

            every { concertRepositoryImpl.findAll() } returns listOf()

            it("예약 가능한 콘서트가 존재하지 않는다") {
                shouldThrow<RuntimeException> {
                    concertReader.getAll()
                }.message shouldBe "콘서트가 존재하지 않습니다."
            }
        }
    }

    describe("getByConcertNumber") {
        context("콘서트 번호를 통해") {

            every {
                concertRepositoryImpl.findByConcertNumber(any())
            } returns Concert(name = "고척돔", number = "1234")

            it("해당하는 콘서트 정보를 반환한다") {
                val concert = concertReader.getByConcertNumber(concertNumber = "1234")
                concert.name shouldBe "고척돔"
                concert.number shouldBe "1234"
            }
        }
    }

    describe("getByConcertNumber 실패") {
        context("콘서트 번호를 통해 조회를 시도하지만") {

            every {
                concertRepositoryImpl.findByConcertNumber(any())
            } returns null

            it("해당하는 콘서트 정보를 반환에 실패한다") {
                shouldThrow<RuntimeException> {
                    concertReader.getByConcertNumber(concertNumber = "1234")
                }.message shouldBe "콘서트가 존재하지 않습니다."
            }
        }
    }

    describe("getByConcertNumberAndDate 실패") {
        context("콘서트 번호와 날짜를 통해 조회를 시도하지만") {

            every {
                concertRepositoryImpl.findByConcertNumberAndDate(any(), any())
            } returns null

            it("해당하는 콘서트 정보를 반환에 실패한다") {
                shouldThrow<RuntimeException> {
                    concertReader.getByConcertNumberAndDate(concertNumber = "1234", date = LocalDate.now())
                }.message shouldBe "콘서트가 존재하지 않습니다."
            }
        }
    }

    describe("getByConcertNumberAndDate") {
        context("콘서트 번호와 날짜를 통해") {

            val concert = Concert(name = "고척돔", number = "1234")
            val bookableDates = mutableListOf(
                BookableDate(date = LocalDate.now(), concert = concert)
            )
            concert.updateBookableDates(bookableDates)

            every {
                concertRepositoryImpl.findByConcertNumberAndDate(any(), any())
            } returns concert

            it("해당하는 콘서트를 조회한다") {
                concertReader.getByConcertNumberAndDate(concertNumber = "1234", date = LocalDate.now())
                concert.name shouldBe "고척돔"
                concert.number shouldBe "1234"
                concert.bookableDates[0].date shouldBe LocalDate.now()
            }
        }
    }

})
