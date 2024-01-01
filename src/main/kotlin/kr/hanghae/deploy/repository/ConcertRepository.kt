package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.User
import java.time.LocalDate

interface ConcertRepository {
    fun findByConcertNumber(concertNumber: String): Concert?
    fun findByConcertNumberAndDate(concertNumber: String, date: LocalDate): Concert?
}
