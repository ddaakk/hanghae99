package kr.hanghae.deploy.scheduler

import kr.hanghae.deploy.repository.BookingRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class BookableScheduler(
    private val bookingRepository: BookingRepository,
) {
    @Scheduled(fixedDelay = 10000)
    @Transactional
    fun updateBookedSeatToBookable() {
        val beforeFiveMinutes: LocalDateTime = LocalDateTime.now().minusMinutes(5)

        bookingRepository.findByUpdatedTime(beforeFiveMinutes).forEach {
            it.changeToBookable()
        }
    }
}
