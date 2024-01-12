package kr.hanghae.deploy.repository

import jakarta.persistence.LockModeType
import kr.hanghae.deploy.domain.User
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface UserRepository {
    fun findByUUID(uuid: String): User?

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select u from User u where u.uuid = :uuid")
    fun findByUUIDWithLock(uuid: String): User?

    fun existByUUID(uuid: String): Boolean

    fun deleteByUUID(uuid: String)
}
