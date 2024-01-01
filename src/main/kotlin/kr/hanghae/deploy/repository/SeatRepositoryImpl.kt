package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.Seat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDate
import java.time.LocalDateTime

interface SeatRepositoryImpl : JpaRepository<Seat, Long>, SeatRepository {
    @Query("select s from Seat s where s.bookableDateId = :bookableDateId")
    override fun findByBookableDateId(bookableDateId: Long): List<Seat>

    @Query("select s from Seat s where s.number in :seatNumbers and s.bookableDateId = :bookableDateId")
    override fun findByOrderAndDate(seatNumbers: List<Int>, bookableDateId: Long): List<Seat>
}
