package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.BookableDate
import org.springframework.stereotype.Repository

interface BookableDateRepository {
    fun findByDate(): List<BookableDate>
    fun findByDate(date: String): BookableDate?
}
