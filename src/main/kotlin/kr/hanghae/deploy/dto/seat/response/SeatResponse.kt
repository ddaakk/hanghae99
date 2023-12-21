package kr.hanghae.deploy.dto.seat.response

import kr.hanghae.deploy.domain.payment.PayStatus
import kr.hanghae.deploy.domain.seat.Seat
import kr.hanghae.deploy.domain.seat.SeatStatus
import kr.hanghae.deploy.domain.user.User

data class SeatResponse(
    val uuid: String?,
    val seatOrder: Long,
    val seatStatus: SeatStatus,
    val price: Long,
    val paymentStatus: PayStatus?,
    val date: String,
) {

    companion object {
        fun of(seat: Seat): SeatResponse {
            return SeatResponse(
                uuid = seat.booking?.user?.uuid,
                seatOrder = seat.orders,
                seatStatus = seat.seatStatus,
                price = seat.price,
                paymentStatus = seat.payment?.payStatus,
                date = seat.bookableDate.date,
            )
        }
    }
}
