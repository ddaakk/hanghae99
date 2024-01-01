package kr.hanghae.deploy.controller

import kr.hanghae.deploy.annotation.QueryStringArgResolver
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.seat.SeatServiceRequest
import kr.hanghae.deploy.dto.seat.request.SeatRequest
import kr.hanghae.deploy.dto.seat.response.SeatResponse
import kr.hanghae.deploy.service.SeatService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors

@RestController
class SeatController(
    private val seatService: SeatService,
) {
    @GetMapping("/api/v1/seat")
    fun getSeatsByConcertAndDate(
        @QueryStringArgResolver request: SeatRequest,
    ): ApiResponse<List<SeatResponse>> {
        return ApiResponse.ok(
            data = SeatResponse.of(
                seatService.getSeatsByConcertAndDate(
                    request = SeatServiceRequest.toService(
                        request.concertNumber,
                        request.date
                    )
                )
            )
        )
    }
}
