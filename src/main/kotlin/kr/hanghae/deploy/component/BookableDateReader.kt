package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.repository.BookableDateRepository
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class BookableDateReader(
    private val bookableDateRepository: BookableDateRepository,
) {

    fun reader(): List<BookableDate> {
       return bookableDateRepository.findByDate().also {
           if (it.isEmpty()) {
               throw RuntimeException("예약 가능한 날이 존재하지 않습니다.")
           }
       }
    }

    fun getByDate(date: String): BookableDate {
        return bookableDateRepository.findByDate(date) ?: throw RuntimeException("예약할 수 없는 날짜입니다.")
    }

}
