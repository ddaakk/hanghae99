package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.User

interface UserRepository {
    fun findByUUID(uuid: String): User?

    fun existByUUID(uuid: String): Boolean
}
