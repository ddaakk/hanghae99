package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface ConcertRepositoryImpl : JpaRepository<Concert, Long>, ConcertRepository {
    @Query("select c from Concert c join fetch c.bookableDates where c.number = :concertNumber")
    override fun findByConcertNumber(concertNumber: String): Concert?

    @Query("select c from Concert c join fetch c.bookableDates b where c.number = :concertNumber and b.date = :date")
    override fun findByConcertNumberAndDate(concertNumber: String, date: LocalDate): Concert?
}
