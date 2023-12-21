package kr.hanghae.deploy.domain.seat

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface SeatRepositoryImpl : JpaRepository<Seat, Long>, SeatRepository {
    @Query("select s from Seat s join s.bookableDate b where b.date = :date")
    override fun findAllByDate(date: String): List<Seat>

    @Query("select s from Seat s join fetch s.bookableDate join fetch s.user " +
        "where s.orders in :seats and s.bookableDate.date = :date")
    override fun findByOrderAndDate(seats: List<Long>, date: String): MutableList<Seat>

    @Query("select s from Seat s join s.bookableDate where s.seatStatus = 'BOOKED' and s.updatedAt < :time")
    override fun findBookedSeat(time: LocalDateTime): List<Seat>
}
