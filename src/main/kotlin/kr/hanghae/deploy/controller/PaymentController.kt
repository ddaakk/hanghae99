package kr.hanghae.deploy.controller

import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.payment.PayBookingServiceRequest
import kr.hanghae.deploy.dto.payment.request.PaymentRequest
import kr.hanghae.deploy.dto.payment.response.PaymentResponse
import kr.hanghae.deploy.service.PaymentService
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
class PaymentController(
    private val paymentService: PaymentService,
) {

    @PostMapping("/api/v1/pay")
    fun payBooking(
        @RequestBody request: PaymentRequest,
        @RequestHeader("Authorization") uuid: String,
    ): ApiResponse<PaymentResponse> {
        return ApiResponse.created(
            data = PaymentResponse.of(
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
