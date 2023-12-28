package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepositoryImpl : JpaRepository<User, Long>, UserRepository {
    @Query("select u from User u where u.uuid = :uuid")
    override fun findByUUID(uuid: String): User?

    @Query("select case when exists (select 1 from User u where u.uuid = :uuid) then true else false end")
    override fun existByUUID(uuid: String): Boolean
}
