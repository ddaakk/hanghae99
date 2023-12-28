package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.BookingSeat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BookingSeatRepositoryImpl : JpaRepository<BookingSeat, Long>, BookingSeatRepository {
    @Query("select count(b) from BookingSeat b where b.seat.id in :seatIds")
    override fun countBySeatIds(seatIds: List<Long>): Long
}
