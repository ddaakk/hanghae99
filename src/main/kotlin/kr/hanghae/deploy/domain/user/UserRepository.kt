package kr.hanghae.deploy.domain.user

interface UserRepository {
    fun findByUuid(uuid: String): User?

    fun deleteByUuid(uuid: String)
}
