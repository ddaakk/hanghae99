package kr.hanghae.deploy.controller

import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.booking.BookingServiceRequest
import kr.hanghae.deploy.dto.booking.request.BookingRequest
import kr.hanghae.deploy.dto.booking.response.BookingResponse
import kr.hanghae.deploy.service.BookingService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class BookingController(
    private val bookingService: BookingService,
) {
    @PostMapping("/api/v1/book")
    fun requestBooking(
        @RequestBody request: BookingRequest,
        @RequestHeader("Authorization") uuid: String,
    ): ApiResponse<BookingResponse> {
        return ApiResponse.created(
            data = bookingService.requestBooking(
                request = BookingServiceRequest.toService(
                    date = request.date,
                    seatOrder = request.seatOrder,
                    uuid = uuid,
                )
            )
        )
    }
}
