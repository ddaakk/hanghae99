package kr.hanghae.deploy.service

import kr.hanghae.deploy.component.BookableDateReader
import kr.hanghae.deploy.component.ConcertReader
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.dto.bookabledate.BookableDateServiceRequest
import kr.hanghae.deploy.dto.bookabledate.response.BookableDateResponse
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


private val logger = KotlinLogging.logger {}

@Service
class BookableDateService(
    private val concertReader: ConcertReader,
) {

    @Transactional(readOnly = true)
    fun getBookableDates(request: BookableDateServiceRequest): List<BookableDate> {
        return concertReader.getByConcertNumber(request.concertNumber).bookableDates
    }
}
