package kr.hanghae.deploy.controller

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.junit5.MockKExtension
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.bookabledate.response.BookableDateResponse
import kr.hanghae.deploy.filter.AuthFilter
import kr.hanghae.deploy.service.BookableDateService
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
    controllers = [BookableDateController::class],
    excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [AuthFilter::class])]
)
@ExtendWith(MockKExtension::class)
internal class BookableDateControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @MockkBean private val bookableDateService: BookableDateService,
) : DescribeSpec({

    describe("getBookableDates") {
        context("예약 가능한 날들을 등록하고") {
            val concert = Concert(name = "고척돔")
            val firstBookableDate = BookableDate(concert = concert, date = LocalDate.now())
            val secondBookableDate = BookableDate(concert = concert, date = LocalDate.now())
            val bookableDates = listOf(firstBookableDate, secondBookableDate)

            every { bookableDateService.getBookableDates(any()) } returns bookableDates

            it("전체 예약 가능 일자들을 조회한다") {

                val actions = mockMvc.get("/api/v1/bookabledate?concertNumber=1234")

                actions.andExpect {
                    status { MockMvcResultMatchers.status().isOk }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        ApiResponse.ok(
                            data = listOf(
                                BookableDateResponse.of(firstBookableDate),
                                BookableDateResponse.of(secondBookableDate)
                            )
                        )
                    }
                    print { MockMvcResultHandlers.print() }
                }.andExpect {
                    jsonPath("\$.code") { value(200) }
                    jsonPath("\$.status") { value("OK") }
                    jsonPath("\$.message") { value("OK") }
                    jsonPath("\$.data.sql[0].date") { value(LocalDate.now().toStr()) }
                    jsonPath("\$.data.sql[1].date") { value(LocalDate.now().toStr()) }
                }.andReturn()
            }
        }
    }
})
