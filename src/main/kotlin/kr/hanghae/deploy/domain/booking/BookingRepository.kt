package kr.hanghae.deploy.domain.booking

import kr.hanghae.deploy.domain.bookabledate.BookableDate
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface BookingRepository : JpaRepository<Booking, Long>
