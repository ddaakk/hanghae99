package kr.hanghae.deploy.service

import kr.hanghae.deploy.component.ConcertReader
import kr.hanghae.deploy.domain.Concert
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ConcertService(
    private val concertReader: ConcertReader,
) {

    @Transactional(readOnly = true)
    fun getConcerts(): List<Concert> {
        return concertReader.getAll()
    }
}
