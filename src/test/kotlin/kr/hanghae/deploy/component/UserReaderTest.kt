package kr.hanghae.deploy.component

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.repository.UserRepository
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class UserReaderTest: DescribeSpec({

    val userRepository = mockk<UserRepository>()
    val userReader = UserReader(userRepository)

    describe("getByUUID") {
        context("사용자 UUID 정보가 주어지면") {
            val user = User(uuid = "uuid")

            every { userRepository.findByUUID("uuid") } returns user

            it("해당 사용자의 정보를 반환한다") {
                val user = userReader.getByUUID(uuid = "uuid")
                user.uuid shouldBe "uuid"
            }
        }
    }

    describe("getByUUID 실패") {
        context("존재하지 않는 사용자의 UUID가 주어지면") {

            every { userRepository.findByUUID("invalid-uuid") } returns null

            it("해당 사용자의 정보를 반환한다") {
                shouldThrow<RuntimeException> {
                    userReader.getByUUID(uuid = "invalid-uuid")
                }.message shouldBe "사용자를 찾을 수 없습니다."
            }
        }
    }
})
