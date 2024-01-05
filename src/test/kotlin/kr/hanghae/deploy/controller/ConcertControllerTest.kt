package kr.hanghae.deploy.controller

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.junit5.MockKExtension
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.bookabledate.BookableDateServiceRequest
import kr.hanghae.deploy.dto.bookabledate.response.BookableDateResponse
import kr.hanghae.deploy.dto.concert.response.ConcertResponse
import kr.hanghae.deploy.filter.AuthFilter
import kr.hanghae.deploy.service.BookableDateService
import kr.hanghae.deploy.service.ConcertService
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate

@WebMvcTest(
    controllers = [ConcertController::class],
    excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [AuthFilter::class])]
)
@ExtendWith(MockKExtension::class)
internal class ConcertControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @MockkBean private val concertService: ConcertService,
) : DescribeSpec({

    describe("getConcerts") {
        context("콘서트들을 등록하고") {
            val firstConcert = Concert(name = "고척돔", number = "1234")
            val secondConcert = Concert(name = "아레나", number = "5678")

            every { concertService.getConcerts() } returns listOf(firstConcert, secondConcert)

            it("전체 콘서트들을 조회한다") {

                val actions = mockMvc.get("/api/v1/concert")

                actions.andExpect {
                    status { MockMvcResultMatchers.status().isOk }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        ApiResponse.ok(
                            data = listOf(
                                ConcertResponse.of(firstConcert),
                                ConcertResponse.of(secondConcert)
                            )
                        )
                    }
                    print { MockMvcResultHandlers.print() }
                }.andExpect {
                    jsonPath("\$.code") { value(200) }
                    jsonPath("\$.status") { value("OK") }
                    jsonPath("\$.message") { value("OK") }
                    jsonPath("\$.data[0].concertNumber") { value("1234") }
                    jsonPath("\$.data[0].name") { value("고척돔") }
                    jsonPath("\$.data[1].concertNumber") { value("5678") }
                    jsonPath("\$.data[1].name") { value("아레나") }
                }.andReturn()
            }
        }
    }
})
