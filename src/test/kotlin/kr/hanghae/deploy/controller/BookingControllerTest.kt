package kr.hanghae.deploy.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.junit5.MockKExtension
import kr.hanghae.deploy.domain.Booking
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.booking.BookingServiceResponse
import kr.hanghae.deploy.dto.booking.request.BookingRequest
import kr.hanghae.deploy.dto.booking.response.BookingResponse
import kr.hanghae.deploy.filter.AuthFilter
import kr.hanghae.deploy.service.BookingService
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDate

@WebMvcTest(
    controllers = [BookingController::class],
    excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [AuthFilter::class])]
)
@ExtendWith(MockKExtension::class)
internal class BookingControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @MockkBean private val bookingService: BookingService,
    @Autowired private val objectMapper: ObjectMapper,
) : DescribeSpec({

    describe("requestBooking") {
        context("예약 가능한 날과 좌석 번호들이 주어지면") {

            val firstSeat = Seat(bookableDateId = 1, number = 1, price = 1000, grade = "A")
            val secondSeat = Seat(bookableDateId = 1, number = 2, price = 2000, grade = "A")
            val thirdSeat = Seat(bookableDateId = 1, number = 3, price = 3000, grade = "A")

            every { bookingService.requestBooking(any()) } returns
                BookingServiceResponse(
                    "uuid",
                    Booking(
                        userId = 1,
                        date = LocalDate.now(),
                        seats = mutableListOf(firstSeat, secondSeat, thirdSeat),
                        number = "1234"
                    )
                )

            it("해당하는 좌석들을 임시 예약한다.") {
                val actions = mockMvc.post("/api/v1/book") {
                    accept = MediaType.APPLICATION_JSON
                    contentType = MediaType.APPLICATION_JSON
                    val request = BookingRequest(
                        concertNumber = "1234",
                        date = LocalDate.now(), seatNumbers = listOf(1, 2, 3)
                    )
                    content = objectMapper.writeValueAsString(request)
                    header("Authorization", "uuid")
                }

                actions.andExpect {
                    status { MockMvcResultMatchers.status().isOk }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        ApiResponse.ok(
                            data =
                            BookingResponse(
                                uuid = "uuid",
                                date = LocalDate.now(),
                                totalPrice = 6000L,
                                bookingNumber = "0000",
                                seatNumbers = listOf(1, 2, 3)
                            )
                        )
                    }
                    print { MockMvcResultHandlers.print() }
                }.andExpect {
                    jsonPath("\$.code") { value(201) }
                    jsonPath("\$.status") { value("CREATED") }
                    jsonPath("\$.message") { value("CREATED") }
                    jsonPath("\$.data.uuid") { value("uuid") }
                    jsonPath("\$.data.date") { value(LocalDate.now().toStr()) }
                    jsonPath("\$.data.totalPrice") { value(6000) }
                    jsonPath("\$.data.bookingNumber") { value("1234") }
                    jsonPath("\$.data.seatNumbers") { isArray() }
                    jsonPath("\$.data.seatNumbers[0]") { value(1) }
                    jsonPath("\$.data.seatNumbers[1]") { value(2) }
                    jsonPath("\$.data.seatNumbers[2]") { value(3) }
                }.andReturn()
            }
        }
    }
})
