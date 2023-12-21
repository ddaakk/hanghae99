package kr.hanghae.deploy.domain.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepositoryImpl : JpaRepository<User, Long>, UserRepository {
    override fun findByUuid(uuid: String): User?

    override fun deleteByUuid(uuid: String)
}
