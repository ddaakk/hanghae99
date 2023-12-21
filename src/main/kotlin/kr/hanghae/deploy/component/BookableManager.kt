package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.seat.SeatRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class BookableManager(
    private val seatRepository: SeatRepository,
) {
    @Scheduled(fixedDelay = 1000)
    @Transactional
    fun updateBookedSeatToBookable() {
        val beforeFiveMinutes: LocalDateTime = LocalDateTime.now().minusMinutes(5)
        seatRepository.findBookedSeat(beforeFiveMinutes).map { seat -> seat.changeToBookable() }
    }
}
