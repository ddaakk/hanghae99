package kr.hanghae.deploy.service

import kr.hanghae.deploy.component.BookableDateReader
import kr.hanghae.deploy.dto.bookabledate.response.BookableDateResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
@Transactional(readOnly = true)
class BookableDateService(
    private val bookableDateReader: BookableDateReader,
) {

    fun getBookableDates(): List<BookableDateResponse> {
        val bookableDates = bookableDateReader.reader()
        return bookableDates.stream().map(BookableDateResponse::of).collect(Collectors.toList())
    }
}
