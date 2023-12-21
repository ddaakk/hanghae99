package kr.hanghae.deploy.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.maps.haveValues
import io.mockk.every
import io.mockk.junit5.MockKExtension
import kr.hanghae.deploy.domain.bookabledate.BookableDate
import kr.hanghae.deploy.domain.seat.Seat
import kr.hanghae.deploy.domain.user.User
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.booking.BookingServiceRequest
import kr.hanghae.deploy.dto.booking.request.BookingRequest
import kr.hanghae.deploy.dto.booking.response.BookingResponse
import kr.hanghae.deploy.dto.seat.SeatServiceRequest
import kr.hanghae.deploy.dto.seat.request.SeatRequest
import kr.hanghae.deploy.dto.seat.response.SeatResponse
import kr.hanghae.deploy.filter.AuthFilter
import kr.hanghae.deploy.service.BookingService
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
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@WebMvcTest(
    controllers = [BookingController::class],
    excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [AuthFilter::class])]
)
@ExtendWith(MockKExtension::class)
class BookingControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @MockkBean private val bookingService: BookingService,
    @Autowired private val objectMapper: ObjectMapper,
) : DescribeSpec({

    describe("requestBooking") {
        context("예약 가능한 날과 좌석 번호들이 주어지면") {
            val bookableDate = BookableDate(date = "2023-12-01")
            val firstSeat = Seat(bookableDate = bookableDate, orders = 1, price = 1000)
            val secondSeat = Seat(bookableDate = bookableDate, orders = 2, price = 2000)
            val thirdSeat = Seat(bookableDate = bookableDate, orders = 3, price = 3000)

            val seats = listOf(
                SeatResponse.of(seat = firstSeat),
                SeatResponse.of(seat = secondSeat),
                SeatResponse.of(seat = thirdSeat),
            )

            val request = BookingRequest("2023-12-01", listOf(1, 2, 3))
            val response = ApiResponse.ok(data = seats)

            every {
                bookingService.requestBooking(
                    BookingServiceRequest("2023-12-01", listOf(1, 2, 3), "uuid")
                )
            } returns BookingResponse(
                uuid = "uuid",
                date = "2023-12-01",
                totalPrice = 6000,
                bookingNumber = "1234",
                seatOrder = listOf(1, 2, 3)
            )

            it("해당하는 좌석들을 임시 예약한다.") {
                val actions = mockMvc.post("/api/v1/book") {
                    accept = MediaType.APPLICATION_JSON
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                    header("Authorization", "uuid")
                }

                actions.andExpect {
                    status { MockMvcResultMatchers.status().isOk }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content { response }
                    print { MockMvcResultHandlers.print() }
                }.andExpect {
                    jsonPath("\$.code") { value(201) }
                    jsonPath("\$.status") { value("CREATED") }
                    jsonPath("\$.message") { value("CREATED") }
                    jsonPath("\$.data.uuid") { value("uuid") }
                    jsonPath("\$.data.date") { value("2023-12-01") }
                    jsonPath("\$.data.totalPrice") { value(6000) }
                    jsonPath("\$.data.bookingNumber") { value("1234") }
                    jsonPath("$.data.seatOrder") { isArray() }
                    jsonPath("$.data.seatOrder[0]") { value(1) }
                    jsonPath("$.data.seatOrder[1]") { value(2) }
                    jsonPath("$.data.seatOrder[2]") { value(3) }
                }.andReturn()
            }
        }
    }
})
