package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.repository.BookableDateRepository
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.time.LocalDate

@Component
class BookableDateReader(
    private val bookableDateRepository: BookableDateRepository,
) {
    fun getByDate(date: LocalDate): BookableDate {
        return bookableDateRepository.findByDate(date) ?: throw RuntimeException("예약할 수 없는 날짜입니다.")
    }

    fun getByConcertAndDate(concertNumber: String, date: LocalDate): BookableDate {
        return bookableDateRepository.findByConcertAndDate(concertNumber, date)
            ?: throw RuntimeException("예약할 수 없는 날짜입니다.")
    }

}
