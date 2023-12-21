package kr.hanghae.deploy.domain.seat

import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface SeatRepository {
    fun findAllByDate(date: String): List<Seat>

    fun findByOrderAndDate(seats: List<Long>, date: String): MutableList<Seat>

    fun findBookedSeat(time: LocalDateTime): List<Seat>
}
