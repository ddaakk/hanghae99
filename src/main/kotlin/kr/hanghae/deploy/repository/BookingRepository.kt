package kr.hanghae.deploy.repository

import jakarta.persistence.LockModeType
import kr.hanghae.deploy.domain.Booking
import org.springframework.data.jpa.repository.Lock
import java.time.LocalDateTime

interface BookingRepository {
    fun findByBookingNumber(bookingNumber: String, userId: Long): Booking?

    fun findByUpdatedTime(time: LocalDateTime): List<Booking>

    fun findByBookingNumberWithLock(bookingNumber: String, userId: Long): Booking?
}
