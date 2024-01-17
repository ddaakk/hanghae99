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
import kr.hanghae.deploy.repository.UserRepositoryImpl
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.Duration

@ExtendWith(MockKExtension::class)
internal class UserServiceTest : DescribeSpec({

    val userReader = mockk<UserReader>()
    val userManager = mockk<UserManager>()
    val redisService = mockk<RedisService>()
    val userRepositoryImpl = mockk<UserRepositoryImpl>()
    val userService = UserService(userReader, userManager, redisService, userRepositoryImpl)

    describe("generateToken") {
        context("대기열 완료에 100명이 넘지 않아서 완료에 새로운 사용자를 추가하고") {

            every { redisService.calculateEntranceTime(any()) } returns "0"
            every { redisService.addValue(any(), any()) } just runs
            every { redisService.getCounter() } returns 0
            every { redisService.calculateRemainTime() } returns "0"
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

            every { userReader.getByUUIDWithLock(any()) } returns user
            every { userRepositoryImpl.saveAndFlush(any()) } returns user

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
