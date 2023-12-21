package kr.hanghae.deploy.controller

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.junit5.MockKExtension
import kr.hanghae.deploy.domain.bookabledate.BookableDate
import kr.hanghae.deploy.domain.seat.Seat
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.seat.SeatServiceRequest
import kr.hanghae.deploy.dto.seat.request.SeatRequest
import kr.hanghae.deploy.dto.seat.response.SeatResponse
import kr.hanghae.deploy.filter.AuthFilter
import kr.hanghae.deploy.service.SeatService
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap

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
            val firstSeat = Seat(bookableDate = bookableDate, orders = 1)
            val secondSeat = Seat(bookableDate = bookableDate, orders = 2)
            val thirdSeat = Seat(bookableDate = bookableDate, orders = 3)

            val seats = listOf(
                SeatResponse.of(seat = firstSeat),
                SeatResponse.of(seat = secondSeat),
                SeatResponse.of(seat = thirdSeat),
            )

            val response = ApiResponse.ok(data = seats)

            every { seatService.getSeatsByDate(SeatServiceRequest(date = "2023-12-01")) } returns seats

            it("좌석들을 조회한다") {
                val actions = mockMvc.get("/api/v1/seat?date=2023-12-01") {
                    accept = MediaType.APPLICATION_JSON
//                    params = LinkedMultiValueMap<String, String>().apply { this["date"] = "2023-12-01" }
//                    header(HttpHeaders.AUTHORIZATION, "uuid")
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
                    jsonPath("\$.data[0].uuid") { isEmpty() }
                    jsonPath("\$.data[0].seatOrder") { value(1) }
                    jsonPath("\$.data[0].price") { value(0) }
                    jsonPath("\$.data[0].paymentStatus") { isEmpty() }
                    jsonPath("\$.data[0].date") { "2023-12-01" }
                    jsonPath("\$.data[1].uuid") { isEmpty() }
                    jsonPath("\$.data[1].seatOrder") { value(2) }
                    jsonPath("\$.data[1].price") { value(0) }
                    jsonPath("\$.data[1].paymentStatus") { isEmpty() }
                    jsonPath("\$.data[1].date") { "2023-12-01" }
                    jsonPath("\$.data[2].uuid") { isEmpty() }
                    jsonPath("\$.data[2].seatOrder") { value(3) }
                    jsonPath("\$.data[2].price") { value(0) }
                    jsonPath("\$.data[2].paymentStatus") { isEmpty() }
                    jsonPath("\$.data[2].date") { "2023-12-01" }
                }.andReturn()
            }
        }
    }
})
