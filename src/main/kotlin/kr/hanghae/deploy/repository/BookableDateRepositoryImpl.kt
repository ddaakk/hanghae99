package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.BookableDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BookableDateRepositoryImpl : JpaRepository<BookableDate, Long>, BookableDateRepository {
    @Query("select b from BookableDate b left join b.seats")
    override fun findByDate(): List<BookableDate>
    override fun findByDate(date: String): BookableDate?
}
