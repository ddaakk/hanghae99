package kr.hanghae.deploy.component

import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.repository.ConcertRepositoryImpl
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ConcertReader(
    private val concertRepositoryImpl: ConcertRepositoryImpl,
) {

    fun getAll(): List<Concert> {
        return concertRepositoryImpl.findAll().also {
            if (it.isEmpty()) {
                throw RuntimeException("콘서트가 존재하지 않습니다.")
            }
        }
    }

    fun getByConcertNumber(concertNumber: String): Concert {
        return concertRepositoryImpl.findByConcertNumber(concertNumber)
            ?: throw RuntimeException("콘서트가 존재하지 않습니다.")
    }

    fun getByConcertNumberAndDate(concertNumber: String, date: LocalDate): Concert {
        return concertRepositoryImpl.findByConcertNumberAndDate(concertNumber, date)
            ?: throw RuntimeException("콘서트가 존재하지 않습니다.")
    }
}
