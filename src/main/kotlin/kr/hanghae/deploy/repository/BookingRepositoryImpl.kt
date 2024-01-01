package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.Booking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface BookingRepositoryImpl : JpaRepository<Booking, Long>, BookingRepository {
    @Query("select b from Booking b join fetch b.seats where b.number = :bookingNumber and b.userId = :userId")
    override fun findByBookingNumber(bookingNumber: String, userId: Long): Booking?

    @Query("select b from Booking b join fetch b.seats where b.status = 'BOOKING' and b.updatedAt < :time")
    override fun findByUpdatedTime(time: LocalDateTime): List<Booking>
}
