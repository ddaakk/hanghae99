package kr.hanghae.deploy.controller

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.junit5.MockKExtension
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.seat.SeatServiceRequest
import kr.hanghae.deploy.dto.seat.response.SeatResponse
import kr.hanghae.deploy.filter.AuthFilter
import kr.hanghae.deploy.service.SeatService
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

@WebMvcTest(controllers = [SeatController::class],
    excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [AuthFilter::class])]
)
@ExtendWith(MockKExtension::class)
class SeatControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @MockkBean private val seatService: SeatService,
) : DescribeSpec({

    describe("getSeatsByDate") {
        context("예약 가능한 날에 해당하는") {
            val bookableDate = BookableDate(date = "2023-12-01")
            val concert = Concert("고척돔")
            val firstSeat = Seat(bookableDate = bookableDate, number = 1, concert = concert, grade = "A")
            val secondSeat = Seat(bookableDate = bookableDate, number = 2, concert = concert, grade = "A")
            val thirdSeat = Seat(bookableDate = bookableDate, number = 3, concert = concert, grade = "A")

            val seats = listOf(firstSeat, secondSeat, thirdSeat)

            val response = ApiResponse.ok(data = seats)

            every { seatService.getSeatsByDate(SeatServiceRequest(date = "2023-12-01")) } returns seats

            it("좌석들을 조회한다") {
                val actions = mockMvc.get("/api/v1/seat?date=2023-12-01") {
                    accept = MediaType.APPLICATION_JSON
                }

                actions.andExpect {
                    status { MockMvcResultMatchers.status().isOk }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content { response }
                    print { MockMvcResultHandlers.print() }
                }.andExpect {
                    jsonPath("\$.code") { value(200) }
                    jsonPath("\$.status") { value("OK") }
                    jsonPath("\$.message") { value("OK") }
                    jsonPath("\$.data[0].seatNumber") { value(1) }
                    jsonPath("\$.data[0].price") { value(0) }
                    jsonPath("\$.data[0].concertName") { value("고척돔") }
                    jsonPath("\$.data[0].bookingStatus") { isEmpty() }
                    jsonPath("\$.data[0].date") { "2023-12-01" }
                    jsonPath("\$.data[1].seatNumber") { value(2) }
                    jsonPath("\$.data[1].price") { value(0) }
                    jsonPath("\$.data[1].concertName") { value("고척돔") }
                    jsonPath("\$.data[1].bookingStatus") { isEmpty() }
                    jsonPath("\$.data[1].date") { "2023-12-01" }
                    jsonPath("\$.data[2].seatNumber") { value(3) }
                    jsonPath("\$.data[2].price") { value(0) }
                    jsonPath("\$.data[2].concertName") { value("고척돔") }
                    jsonPath("\$.data[2].bookingStatus") { isEmpty() }
                    jsonPath("\$.data[2].date") { "2023-12-01" }
                }.andReturn()
            }
        }
    }
})
