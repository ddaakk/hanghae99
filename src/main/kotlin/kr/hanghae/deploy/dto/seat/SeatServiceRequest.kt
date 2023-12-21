package kr.hanghae.deploy.dto.seat

import kr.hanghae.deploy.domain.seat.Seat
import kr.hanghae.deploy.dto.seat.response.SeatResponse

data class SeatServiceRequest(
    val date: String,
) {
    companion object {
        fun toService(date: String): SeatServiceRequest {
            return SeatServiceRequest(
                date = date
            )
        }
    }
}
