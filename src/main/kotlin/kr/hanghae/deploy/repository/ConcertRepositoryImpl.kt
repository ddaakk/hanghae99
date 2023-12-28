package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ConcertRepositoryImpl : JpaRepository<Concert, Long>, ConcertRepository {
}
