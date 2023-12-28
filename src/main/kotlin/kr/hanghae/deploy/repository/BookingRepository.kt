package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.Booking

interface BookingRepository {
    fun findByBookingNumber(bookingNumber: String, uuid: String): Booking?
}
