package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface UserRepositoryImpl : JpaRepository<User, Long>, UserRepository {
    @Query("select u from User u where u.uuid = :uuid")
    override fun findByUUID(uuid: String): User?

    @Query("select case when exists (select 1 from User u where u.uuid = :uuid) then true else false end")
    override fun existByUUID(uuid: String): Boolean

    @Transactional
    @Modifying
    @Query("delete from User u where u.uuid = :uuid")
    override fun deleteByUUID(uuid: String)
}
