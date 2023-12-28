package kr.hanghae.deploy.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.junit5.MockKExtension
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.seat.SeatServiceRequest
import kr.hanghae.deploy.dto.user.ChargeBalanceServiceRequest
import kr.hanghae.deploy.dto.user.GetBalanceServiceRequest
import kr.hanghae.deploy.dto.user.request.ChargeBalanceRequest
import kr.hanghae.deploy.dto.user.response.ChargeBalanceResponse
import kr.hanghae.deploy.dto.user.response.GetBalanceResponse
import kr.hanghae.deploy.filter.AuthFilter
import kr.hanghae.deploy.service.UserService
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap

@WebMvcTest(controllers = [UserController::class],
    excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [AuthFilter::class])]
)
@ExtendWith(MockKExtension::class)
class UserControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @MockkBean private val userService: UserService,
    @Autowired objectMapper: ObjectMapper,
) : DescribeSpec({

    describe("chargeBalance") {
        context("사용자 UUID와 금액이 주어지면") {
            val user = User(uuid = "uuid", balance = 1000L)

            val response = ApiResponse.ok(data = ChargeBalanceResponse(uuid = "uuid", balance = 1000L))

            every { userService.chargeBalance(any()) } returns user

            it("사용자 잔액을 충전한다") {
                val actions = mockMvc.put("/api/v1/user/charge") {
                    accept = MediaType.APPLICATION_JSON
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(ChargeBalanceRequest(balance = 1000L))
                    header(HttpHeaders.AUTHORIZATION, "uuid")
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
                    jsonPath("\$.data.uuid") { "uuid" }
                    jsonPath("\$.data.balance") { value(1000) }
                }.andReturn()
            }
        }
    }

    describe("getBalance") {
        context("사용자 UUID와 금액이 주어지면") {
            val user = User(uuid = "uuid", balance = 1000L)

            val response = ApiResponse.ok(data = GetBalanceResponse(uuid = "uuid", balance = 1000L))

            every { userService.getBalance(any()) } returns user

            it("사용자 잔액을 조회한다") {
                val actions = mockMvc.get("/api/v1/user/balance") {
                    accept = MediaType.APPLICATION_JSON
                    header(HttpHeaders.AUTHORIZATION, "uuid")
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
                    jsonPath("\$.data.uuid") { "uuid" }
                    jsonPath("\$.data.balance") { value(1000) }
                }.andReturn()
            }
        }
    }
})
