package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.BookableDate
import org.springframework.stereotype.Repository
import java.time.LocalDate

interface BookableDateRepository {
    fun findByDate(date: LocalDate): BookableDate?
    fun findByConcertAndDate(concertNumber: String, date: LocalDate): BookableDate?
}
