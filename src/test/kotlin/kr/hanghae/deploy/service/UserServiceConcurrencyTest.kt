package kr.hanghae.deploy.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.uuid.Generators
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import io.mockk.junit5.MockKExtension
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.hanghae.deploy.DatabaseCleanUpExecutor
import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.dto.booking.BookingServiceRequest
import kr.hanghae.deploy.dto.user.ChargeBalanceServiceRequest
import kr.hanghae.deploy.filter.AuthFilter
import kr.hanghae.deploy.repository.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.jdbc.core.JdbcTemplate
import java.io.FileWriter
import java.io.PrintWriter
import java.io.Writer
import java.lang.RuntimeException
import java.math.BigDecimal
import java.net.http.HttpRequest
import java.time.LocalDate
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random

@SpringBootTest
@ExtendWith(MockKExtension::class)
internal class UserServiceConcurrencyTest(
    private val jdbcTemplate: JdbcTemplate,
    private val userRepositoryImpl: UserRepositoryImpl,
    private val userService: UserService,
    private val redisService: RedisService,
) : DescribeSpec() {

    override suspend fun beforeSpec(spec: Spec) {
        redisService.flushAll()
        redisService.addValue("counter", "0")
        redisService.addValue("throughput", "8000")
        redisService.addValue("cycleInterval", "60000")
    }

    init {

        val request = mockk<HttpServletRequest>()
        val response = mockk<HttpServletResponse>()
        val chain = mockk<FilterChain>()
        val objectMapper = mockk<ObjectMapper>()
        val userReader = mockk<UserReader>()
        val authFilter = AuthFilter(objectMapper, userReader, redisService)

        describe("generateToken") {

            every { request.getHeader(any()) } returns "uuid"
            every { userReader.findByUUID(any()) } returns User("uuid")
            every { objectMapper.writeValueAsString(any()) } returns null
            every { response.characterEncoding = any() } just runs
            every { response.status = any() } just runs
            every { response.contentType = any() } just runs
            every { response.writer } returns mock(PrintWriter::class.java)
            every { chain.doFilter(any(), any()) } just runs

            context("대기열 제한은 100명이지만 200명이 시도하여") {

                val numberOfThreads = 200
                val service: ExecutorService = Executors.newFixedThreadPool(16)
                val latch = CountDownLatch(numberOfThreads)

                repeat(numberOfThreads) {
                    service.execute {
                        try {
                            userService.generateToken()
                        } catch (e: RuntimeException) {
                        } finally {
                            latch.countDown()
                        }
                    }
                }

                latch.await()

                it("대기열에 100명이 쌓인다") {
                    mockkStatic(Generators::class)
                    every { Generators.timeBasedGenerator().generate().toString() } returns "uuid"
                    userService.generateToken()
                    authFilter.doFilter(request, response, chain)
                }
            }
        }

        describe("chargeBalance") {
            context("동시에 같은 계정에 포인트 충전을 시도할 시") {

                userRepositoryImpl.save(User(uuid = "uuid", balance = BigDecimal(0)))

                val numberOfThreads = 10
                val service: ExecutorService = Executors.newFixedThreadPool(numberOfThreads)
                val latch = CountDownLatch(numberOfThreads)

                repeat(numberOfThreads) {
                    service.execute {
                        try {
                            userService.chargeBalance(
                                ChargeBalanceServiceRequest.toService(
                                    balance = BigDecimal(100000),
                                    uuid = "uuid"
                                )
                            )
                        } catch (e: RuntimeException) {
                        } finally {
                            latch.countDown()
                        }
                    }
                }

                latch.await()

                it("한 명만 예약에 성공한다") {
                    userRepositoryImpl.findByUUID("uuid")!!.balance shouldBe BigDecimal(100000).setScale(2)
                }
            }
        }
    }

    override fun listeners(): List<TestListener> = listOf(DatabaseCleanUpExecutor(jdbcTemplate))
}
