package kr.hanghae.deploy.repository

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe
import kr.hanghae.deploy.DatabaseCleanUpExecutor
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.txContext
import kr.hanghae.deploy.repository.UserRepositoryImpl
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@DataJpaTest
internal class UserRepositoryTest(
    private val jdbcTemplate: JdbcTemplate,
    private val userRepositoryImpl: UserRepositoryImpl,
) : DescribeSpec() {

    override suspend fun beforeEach(testCase: TestCase) {
        val user = User(uuid = "uuid")
        userRepositoryImpl.saveAndFlush(user)
    }

    init {
        describe("findByUUID") {
            txContext("사용자 UUID 정보가 주어지면") {
                it("해당 사용자의 정보를 반환한다") {
                    val user = userRepositoryImpl.findByUUID("uuid")
                    user?.uuid shouldBe "uuid"
                }
            }
        }

        describe("existByUUID") {
            txContext("사용자 UUID 정보가 주어지면") {
                it("해당 사용자가 존재하는지 확인한다") {
                    val result = userRepositoryImpl.existByUUID("uuid")
                    result shouldBe true
                }
            }
        }

        describe("deleteByUUID") {
            txContext("사용자 UUID 정보가 주어지면") {
                it("해당 사용자를 삭제한다") {
                    val result = userRepositoryImpl.deleteByUUID("uuid")
                    userRepositoryImpl.findAll().size shouldBe 0
                }
            }
        }
    }

    override fun listeners(): List<TestListener> = listOf(DatabaseCleanUpExecutor(jdbcTemplate))
}
