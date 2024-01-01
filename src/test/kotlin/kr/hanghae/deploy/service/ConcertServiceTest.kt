package kr.hanghae.deploy.service

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.component.ConcertReader
import kr.hanghae.deploy.domain.Concert
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class ConcertServiceTest : DescribeSpec({

    val concertReader = mockk<ConcertReader>()
    val concertService = ConcertService(concertReader)

    describe("getConcerts") {
        context("현재 예약 가능한") {

            every { concertReader.getAll() } returns listOf(
                Concert(name = "고척돔"),
                Concert(name = "아레나")
            )

            it("전체 콘서트 목록을 조회한다") {
                val response = concertService.getConcerts()

                response.size shouldBe 2
                response[0].name shouldBe "고척돔"
                response[1].name shouldBe "아레나"
            }
        }
    }
})
