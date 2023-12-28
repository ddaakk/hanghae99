package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.Seat

interface SeatRepository {
    fun findAllByDate(date: String): List<Seat>

    fun findByOrderAndDate(seats: List<Int>, date: String): MutableList<Seat>

//    fun findBookedSeat(time: LocalDateTime): List<Seat>
}
