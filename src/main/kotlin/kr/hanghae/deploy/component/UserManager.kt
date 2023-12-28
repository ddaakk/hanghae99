package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.repository.UserRepositoryImpl
import org.springframework.stereotype.Component

@Component
class UserManager(
    private val userRepositoryImpl: UserRepositoryImpl,
) {
    fun saveUser(user: User): User {
        return userRepositoryImpl.save(user)
    }
}
