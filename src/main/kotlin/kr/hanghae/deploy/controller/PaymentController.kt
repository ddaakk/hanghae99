package kr.hanghae.deploy.controller

import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.payment.PayBookingServiceRequest
import kr.hanghae.deploy.dto.payment.request.PaymentRequest
import kr.hanghae.deploy.dto.payment.response.PaymentResponse
import kr.hanghae.deploy.service.PaymentService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
class PaymentController(
    private val paymentService: PaymentService,
) {

    @PostMapping("/api/v1/pay")
    @ResponseStatus(HttpStatus.CREATED)
    fun payBooking(
        @RequestBody request: PaymentRequest,
        @RequestHeader("Authorization") uuid: String,
    ): ApiResponse<PaymentResponse> {
        return ApiResponse.created(
            data = PaymentResponse.from(
                paymentService.payBooking(
                    PayBookingServiceRequest.toService(
                        bookingNumber = request.bookingNumber,
                        uuid = uuid
                    )
                )
            )
        )
    }
}
