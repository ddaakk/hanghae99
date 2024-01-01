package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.Seat
import java.time.LocalDate

interface SeatRepository {
    fun findByBookableDateId(bookableDateId: Long): List<Seat>
    fun findByOrderAndDate(seatNumbers: List<Int>, bookableDateId: Long): List<Seat>
}
