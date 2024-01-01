package kr.hanghae.deploy.controller

import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.booking.BookingServiceRequest
import kr.hanghae.deploy.dto.booking.request.BookingRequest
import kr.hanghae.deploy.dto.booking.response.BookingResponse
import kr.hanghae.deploy.service.BookingService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class BookingController(
    private val bookingService: BookingService,
) {
    @PostMapping("/api/v1/book")
    @ResponseStatus(HttpStatus.CREATED)
    fun requestBooking(
        @RequestBody request: BookingRequest,
        @RequestHeader("Authorization") uuid: String,
    ): ApiResponse<BookingResponse> {
        val (concertNumber, date, seatNumbers) = request
        return ApiResponse.created(
            data = BookingResponse.of(
                bookingService.requestBooking(
                    request = BookingServiceRequest.toService(
                        concertNumber, date, seatNumbers, uuid
                    )
                )
            )
        )
    }
}
