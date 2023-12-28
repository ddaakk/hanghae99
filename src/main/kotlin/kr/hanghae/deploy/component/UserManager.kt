package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.repository.UserRepositoryImpl
import org.springframework.stereotype.Component

@Component
class UserManager(
    private val userRepositoryImpl: UserRepositoryImpl,
) {
    fun saveUser(): User {
        return userRepositoryImpl.save(User())
    }
}
