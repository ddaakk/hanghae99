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
import kr.hanghae.deploy.dto.user.GenerateTokenServiceResponse
import kr.hanghae.deploy.dto.user.GetBalanceServiceRequest
import kr.hanghae.deploy.dto.user.request.ChargeBalanceRequest
import kr.hanghae.deploy.dto.user.response.ChargeBalanceResponse
import kr.hanghae.deploy.dto.user.response.GenerateTokenResponse
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
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import java.math.BigDecimal

@WebMvcTest(
    controllers = [UserController::class],
    excludeFilters = [ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [AuthFilter::class])]
)
@ExtendWith(MockKExtension::class)
internal class UserControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @MockkBean private val userService: UserService,
    @Autowired objectMapper: ObjectMapper,
) : DescribeSpec({

    describe("generateToken") {
        context("새로운 사용자를 저장하고") {

            every { userService.generateToken() } returns GenerateTokenServiceResponse(
                uuid = "uuid", waiting = 0, remainTime = 0
            )

            it("UUID 토큰을 발급한다") {
                val actions = mockMvc.post("/api/v1/user/token")

                actions.andExpect {
                    status { MockMvcResultMatchers.status().isOk }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        ApiResponse.ok(
                            data = GenerateTokenResponse("uuid", 0, 0)
                        )
                    }
                    print { MockMvcResultHandlers.print() }
                }.andExpect {
                    jsonPath("\$.code") { value(201) }
                    jsonPath("\$.status") { value("CREATED") }
                    jsonPath("\$.message") { value("CREATED") }
                    jsonPath("\$.data.uuid") { "uuid" }
                    jsonPath("\$.data.waiting") { value(0) }
                    jsonPath("\$.data.remainTime") { value(0) }
                }.andReturn()
            }
        }
    }

    describe("chargeBalance") {
        context("사용자 UUID와 금액이 주어지면") {

            every { userService.chargeBalance(any()) } returns User(uuid = "uuid", BigDecimal(2000))

            it("사용자 잔액을 충전한다") {
                val actions = mockMvc.put("/api/v1/user/charge") {
                    accept = MediaType.APPLICATION_JSON
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(ChargeBalanceRequest(balance = BigDecimal(1000)))
                    header(HttpHeaders.AUTHORIZATION, "uuid")
                }

                actions.andExpect {
                    status { MockMvcResultMatchers.status().isOk }
                    content { contentType(MediaType.APPLICATION_JSON) }
                    content {
                        ApiResponse.ok(
                            data = ChargeBalanceResponse(uuid = "uuid", balance = BigDecimal(2000))
                        )
                    }
                    print { MockMvcResultHandlers.print() }
                }.andExpect {
                    jsonPath("\$.code") { value(200) }
                    jsonPath("\$.status") { value("OK") }
                    jsonPath("\$.message") { value("OK") }
                    jsonPath("\$.data.uuid") { "uuid" }
                    jsonPath("\$.data.balance") { value(2000) }
                }.andReturn()
            }
        }
    }

    describe("getBalance") {
        context("사용자 UUID와 금액이 주어지면") {
            val user = User(uuid = "uuid", balance = BigDecimal(1000))

            val response = ApiResponse.ok(data = GetBalanceResponse(uuid = "uuid", balance = BigDecimal(1000)))

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
