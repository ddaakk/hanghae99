package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.Booking
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BookingRepositoryImpl : JpaRepository<Booking, Long>, BookingRepository {
    @Query("select b from Booking b join fetch b.user join fetch b.seats " +
        "where b.number = :bookingNumber and b.user.uuid = :uuid")
    override fun findByBookingNumber(bookingNumber: String, uuid: String): Booking?
}
