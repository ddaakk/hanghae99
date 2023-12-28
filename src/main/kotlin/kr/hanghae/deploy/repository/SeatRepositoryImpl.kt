package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.Seat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface SeatRepositoryImpl : JpaRepository<Seat, Long>, SeatRepository {
    @Query("select s from Seat s join s.bookableDate b where b.date = :date")
    override fun findAllByDate(date: String): List<Seat>

    @Query("select s from Seat s join fetch s.bookableDate " +
        "where s.number in :seatNumbers and s.bookableDate.date = :date"
    )
    override fun findByOrderAndDate(seatNumbers: List<Int>, date: String): MutableList<Seat>

//    @Query("select s from Seat s join s.bookableDate where s.status = 'BOOKED' and s.updatedAt < :time")
//    override fun findBookedSeat(time: LocalDateTime): List<Seat>
}
