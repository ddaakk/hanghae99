package kr.hanghae.deploy.repository

import kr.hanghae.deploy.domain.BookingSeat

interface BookingSeatRepository {
    fun countBySeatIds(seatIds: List<Long>): Long
}
