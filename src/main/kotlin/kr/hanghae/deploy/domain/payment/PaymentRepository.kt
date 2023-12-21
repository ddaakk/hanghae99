package kr.hanghae.deploy.domain.payment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PaymentRepository : JpaRepository<Payment, Long> {
    @Query("select p from Payment p join p.booking join p.user " +
        "where p.user.uuid = :uuid and p.booking.number = :bookingNumber")
    fun findByBookingNumber(uuid: String, bookingNumber: String): Payment?
}
