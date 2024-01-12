package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.repository.UserRepository
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class UserReader(
    private val userRepository: UserRepository,
) {

    fun getByUUID(uuid: String): User {
        return userRepository.findByUUID(uuid) ?: throw RuntimeException("사용자를 찾을 수 없습니다.")
    }

    fun getByUUIDWithLock(uuid: String): User {
        return userRepository.findByUUIDWithLock(uuid) ?: throw RuntimeException("사용자를 찾을 수 없습니다.")
    }

    fun findByUUID(uuid: String): User? {
        return userRepository.findByUUID(uuid)
    }
}
