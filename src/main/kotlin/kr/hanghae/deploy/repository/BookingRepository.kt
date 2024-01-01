package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.Booking
import java.time.LocalDateTime

interface BookingRepository {
    fun findByBookingNumber(bookingNumber: String, userId: Long): Booking?

    fun findByUpdatedTime(time: LocalDateTime): List<Booking>
}
