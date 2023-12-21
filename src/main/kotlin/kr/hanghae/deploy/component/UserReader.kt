package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.user.User
import kr.hanghae.deploy.domain.user.UserRepository
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class UserReader(
    private val userRepository: UserRepository,
) {

    fun readerByUuid(uuid: String): User {
        return userRepository.findByUuid(uuid) ?: throw RuntimeException("사용자를 찾을 수 없습니다.")
    }

}
