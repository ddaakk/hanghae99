package kr.hanghae.deploy.service

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.component.SeatReader
import kr.hanghae.deploy.component.UserManager
import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.dto.seat.SeatServiceRequest
import kr.hanghae.deploy.dto.seat.response.SeatResponse
import kr.hanghae.deploy.dto.user.ChargeBalanceServiceRequest
import kr.hanghae.deploy.dto.user.GetBalanceServiceRequest
import kr.hanghae.deploy.repository.EmitterRepository
import kr.hanghae.deploy.repository.UserRepositoryImpl
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class UserServiceTest: DescribeSpec({

    val userReader = mockk<UserReader>()
    val userManager = mockk<UserManager>()
    val redisService = mockk<RedisService>()
    val userService = UserService(userReader, userManager, redisService)

    describe("chargeBalance") {
        context("사용자 UUID와 금액이 주어지면") {

            val user = User(uuid = "uuid")
            val request = ChargeBalanceServiceRequest(1000L, "uuid")

            every { userReader.getByUUID(any()) } returns user

            it("사용자 잔액을 충전한다") {
                val user = userService.chargeBalance(request)
                user.balance shouldBe 1000L
            }
        }
    }

    describe("getBalance") {
        context("사용자 UUID와 금액이 주어지면") {

            val user = User(uuid = "uuid", balance = 1000L)
            val request = GetBalanceServiceRequest("uuid")

            every { userReader.getByUUID(any()) } returns user

            it("사용자 잔액을 조회한다") {
                val user = userService.getBalance(request)
                user.balance shouldBe 1000L
            }
        }
    }
})
