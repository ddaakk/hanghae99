package kr.hanghae.deploy.dto.concert.response

import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert

data class ConcertResponse(
    val concertNumber: String,
    val name: String,
) {

    companion object {
        fun of(concert: Concert): ConcertResponse {
            return ConcertResponse(
                concertNumber = concert.number,
                name = concert.name
            )
        }
    }
}
