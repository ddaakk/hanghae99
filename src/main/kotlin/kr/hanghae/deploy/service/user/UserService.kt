package kr.hanghae.deploy.service.user

import kr.hanghae.deploy.domain.user.User
import kr.hanghae.deploy.domain.user.UserRepository
import kr.hanghae.deploy.dto.user.response.ApplyTokenResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
) {

    @Transactional
    fun applyToken(): ApplyTokenResponse {
        val newUser = User()
        val savedUser = userRepository.save(newUser)
        return ApplyTokenResponse.of(savedUser)
    }
}
