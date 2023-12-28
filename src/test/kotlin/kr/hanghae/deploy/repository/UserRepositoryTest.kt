package kr.hanghae.deploy.repository

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.txContext
import kr.hanghae.deploy.repository.UserRepositoryImpl
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
internal class UserRepositoryTest(
    private val userRepositoryImpl: UserRepositoryImpl,
) : DescribeSpec({

    describe("findByUUID") {
        txContext("사용자 UUID 정보가 주어지면") {

            val user = User(uuid = "uuid")
            userRepositoryImpl.saveAndFlush(user)

            it("해당 사용자의 정보를 반환한다") {
                val user = userRepositoryImpl.findByUUID("uuid")

                user?.uuid shouldBe "uuid"
            }
        }
    }
})
