package kr.hanghae.deploy.service

import com.fasterxml.uuid.Generators
import com.fasterxml.uuid.impl.TimeBasedGenerator
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import io.mockk.junit5.MockKExtension
import kr.hanghae.deploy.component.UserManager
import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.dto.user.ChargeBalanceServiceRequest
import kr.hanghae.deploy.dto.user.GetBalanceServiceRequest
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal

@ExtendWith(MockKExtension::class)
internal class UserServiceTest : DescribeSpec({

    val userReader = mockk<UserReader>()
    val userManager = mockk<UserManager>()
    val redisService = mockk<RedisService>()
    val userService = UserService(userReader, userManager, redisService)

    describe("generateToken 대기열 조회 실패") {
        context("대기열 사이즈를 가져올 수 없어서") {

            every { redisService.getQueueSize() } returns null

            it("새로운 토큰 생성에 실패한다") {
                shouldThrow<RuntimeException> {
                    userService.generateToken()
                }.message shouldBe "대기열을 확인할 수 없습니다. 잠시 후 재시도 해주세요."
            }
        }
    }

    describe("generateToken 대기열 사이즈 초과 실패") {
        context("대기열이 가득차서") {

            every { redisService.getQueueSize() } returns 101

            it("새로운 토큰 생성에 실패한다") {
                shouldThrow<RuntimeException> {
                    userService.generateToken()
                }.message shouldBe "대기열이 가득찼습니다. 잠시 후 재시도 해주세요."
            }
        }
    }

    describe("generateToken") {
        context("새로운 유저를 생성하고") {

            every { redisService.getQueueSize() } returns 0
            every { redisService.registerQueue(any()) } just runs
            every { redisService.getQueueOrder(any()) } returns 0
            every { userManager.saveUser(any()) } returns User(uuid = "uuid")

            mockkStatic(Generators::class)
            val timeBasedGeneratorMock = mockk<TimeBasedGenerator>()
            every { Generators.timeBasedGenerator() } returns timeBasedGeneratorMock
            every { timeBasedGeneratorMock.generate().toString() } returns "uuid"

            it("UUID 토큰을 반환한다") {
                val user = userService.generateToken()
                user.uuid shouldBe "uuid"
                user.waiting shouldBe 0
                user.remainTime shouldBe 0
            }
        }
    }

    describe("chargeBalance") {
        context("사용자 UUID와 금액이 주어지면") {

            val user = User(uuid = "uuid")

            every { userReader.getByUUID(any()) } returns user

            it("사용자 잔액을 충전한다") {
                val user = userService.chargeBalance(
                    ChargeBalanceServiceRequest(
                        balance = BigDecimal(1000), uuid = "uuid"
                    )
                )
                user.balance shouldBe BigDecimal(1000)
            }
        }
    }

    describe("getBalance") {
        context("사용자 UUID와 금액이 주어지면") {

            val user = User(uuid = "uuid", balance = BigDecimal(1000))

            every { userReader.getByUUID(any()) } returns user

            it("사용자 잔액을 조회한다") {
                val user = userService.getBalance(
                    GetBalanceServiceRequest(uuid = "uuid")
                )
                user.balance shouldBe BigDecimal(1000)
            }
        }
    }
})
