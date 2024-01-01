package kr.hanghae.deploy.component

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.domain.*
import kr.hanghae.deploy.repository.*
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
class UserManagerTest : DescribeSpec({

    val userRepositoryImpl = mockk<UserRepositoryImpl>()
    val userManager = UserManager(userRepositoryImpl)

    describe("saveUser") {
        context("유저 정보를 통해") {

            every {
                userRepositoryImpl.save(any())
            } returns User(uuid = "uuid", balance = BigDecimal(1000))

            it("유저를 저장한다") {
                val user = userManager.saveUser(User(uuid = "uuid", balance = BigDecimal(1000)))
                user.uuid shouldBe "uuid"
                user.balance shouldBe BigDecimal(1000)
            }
        }
    }
})
