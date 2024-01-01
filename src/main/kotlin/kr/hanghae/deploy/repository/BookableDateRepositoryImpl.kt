package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.BookableDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

interface BookableDateRepositoryImpl : JpaRepository<BookableDate, Long>, BookableDateRepository {
    override fun findByDate(date: LocalDate): BookableDate?

    @Query("select b from BookableDate b join fetch b.concert c where c.number = :concertNumber and b.date = :date")
    override fun findByConcertAndDate(concertNumber: String, date: LocalDate): BookableDate?
}
