package kr.hanghae.deploy.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.junit5.MockKExtension
import kr.hanghae.deploy.domain.*
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.payment.PayBookingServiceResponse
import kr.hanghae.deploy.dto.payment.request.PaymentRequest
import kr.hanghae.deploy.dto.payment.response.PaymentResponse
import kr.hanghae.deploy.filter.AuthFilter
import kr.hanghae.deploy.service.PaymentService
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
    controllers = [PaymentController::class],
    excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [AuthFilter::class])]
)
@ExtendWith(MockKExtension::class)
internal class PaymentControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @MockkBean private val paymentService: PaymentService,
    @Autowired private val objectMapper: ObjectMapper,
) : DescribeSpec({

    describe("payBooking") {
        context("예약 번호를 통해 해당하는 예약에 대한") {

            val firstSeat = Seat(bookableDateId = 1, number = 1, price = 1000, grade = "A")
            val secondSeat = Seat(bookableDateId = 1, number = 2, price = 2000, grade = "A")
            val thirdSeat = Seat(bookableDateId = 1, number = 3, price = 3000, grade = "A")

            every { paymentService.payBooking(any()) } returns
                PayBookingServiceResponse.from(
                    "uuid", Booking(
                        userId = 1,
                        date = LocalDate.now(),
                        seats = mutableListOf(firstSeat, secondSeat, thirdSeat),
                        number = "1234",
                        status = BookingStatus.BOOKED,
                    )
                )

            it("좌석들을 구매하고 예약 상태로 변경한다") {
                val actions = mockMvc.post("/api/v1/pay") {
                    accept = MediaType.APPLICATION_JSON
                    contentType = MediaType.APPLICATION_JSON
                    val request = PaymentRequest(bookingNumber = "1234")
                    content = objectMapper.writeValueAsString(request)
                    header("Authorization", "uuid")
                }

                actions.andExpect {
                    status { MockMvcResultMatchers.status().isOk }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        ApiResponse.ok(
                            data =
                            PaymentResponse(
                                uuid = "uuid",
                                bookingNumber = "1234",
                                seatNumbers = listOf(1, 2, 3),
                                totalPrice = 6000,
                                bookingStatus = BookingStatus.BOOKED
                            )
                        )
                    }
                    print { MockMvcResultHandlers.print() }
                }.andExpect {
                    jsonPath("\$.code") { value(201) }
                    jsonPath("\$.status") { value("CREATED") }
                    jsonPath("\$.message") { value("CREATED") }
                    jsonPath("\$.data.uuid") { value("uuid") }
                    jsonPath("\$.data.bookingNumber") { value("1234") }
                    jsonPath("\$.data.seatNumbers") { listOf(1, 2, 3) }
                    jsonPath("\$.data.totalPrice") { value(6000) }
                    jsonPath("\$.data.bookingStatus") { value("BOOKED") }
                }.andReturn()
            }
        }
    }
})
