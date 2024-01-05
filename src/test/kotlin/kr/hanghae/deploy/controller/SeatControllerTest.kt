package kr.hanghae.deploy.controller

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.InternalPlatformDsl.toStr
import io.mockk.every
import io.mockk.junit5.MockKExtension
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.seat.SeatServiceResponse
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
import java.time.LocalDate

@WebMvcTest(
    controllers = [SeatController::class],
    excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [AuthFilter::class])]
)
@ExtendWith(MockKExtension::class)
internal class SeatControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @MockkBean private val seatService: SeatService,
) : DescribeSpec({

    describe("getSeatsByDate") {
        context("예약 가능한 날에 해당하는") {
            val concert = Concert(name = "고척돔")
            val bookableDate = BookableDate(concert = concert, date = LocalDate.now())
            val firstSeat = Seat(bookableDateId = 1, number = 1, grade = "A")
            val secondSeat = Seat(bookableDateId = 1, number = 2, grade = "A")
            val thirdSeat = Seat(bookableDateId = 1, number = 3, grade = "A")

            val seats = listOf(firstSeat, secondSeat, thirdSeat)

            every {
                seatService.getSeatsByConcertAndDate(any())
            } returns SeatServiceResponse.from(bookableDate, seats)

            it("좌석들을 조회한다") {
                val actions = mockMvc.get(
                    "/api/v1/seat?concertNumber=1234&date=${LocalDate.now().toStr()}"
                ) {
                    accept = MediaType.APPLICATION_JSON
                }

                actions.andExpect {
                    status { MockMvcResultMatchers.status().isOk }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content { ApiResponse.ok(data = SeatResponse.of(SeatServiceResponse.from(bookableDate, seats))) }
                    print { MockMvcResultHandlers.print() }
                }.andExpect {
                    jsonPath("\$.code") { value(200) }
                    jsonPath("\$.status") { value("OK") }
                    jsonPath("\$.message") { value("OK") }
                    jsonPath("\$.data.sql[0].seatNumber") { value(1) }
                    jsonPath("\$.data.sql[0].price") { value(0) }
                    jsonPath("\$.data.sql[0].concertName") { value("고척돔") }
                    jsonPath("\$.data.sql[0].bookingStatus") { value("NOT_BOOKED") }
                    jsonPath("\$.data.sql[0].date") { LocalDate.now().toStr() }
                    jsonPath("\$.data.sql[1].seatNumber") { value(2) }
                    jsonPath("\$.data.sql[1].price") { value(0) }
                    jsonPath("\$.data.sql[1].concertName") { value("고척돔") }
                    jsonPath("\$.data.sql[1].bookingStatus") { value("NOT_BOOKED") }
                    jsonPath("\$.data.sql[1].date") { LocalDate.now().toStr() }
                    jsonPath("\$.data.sql[2].seatNumber") { value(3) }
                    jsonPath("\$.data.sql[2].price") { value(0) }
                    jsonPath("\$.data.sql[2].concertName") { value("고척돔") }
                    jsonPath("\$.data.sql[2].bookingStatus") { value("NOT_BOOKED") }
                    jsonPath("\$.data.sql[2].date") { LocalDate.now().toStr() }
                }.andReturn()
            }
        }
    }
})
