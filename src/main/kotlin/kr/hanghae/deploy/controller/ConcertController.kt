package kr.hanghae.deploy.controller

import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.bookabledate.response.BookableDateResponse
import kr.hanghae.deploy.dto.concert.response.ConcertResponse
import kr.hanghae.deploy.service.ConcertService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors

@RestController
class ConcertController(
    private val concertService: ConcertService,
) {
    @GetMapping("/api/v1/concert")
    fun getConcerts(): ApiResponse<List<ConcertResponse>> {
        return ApiResponse.ok(
            data = concertService.getConcerts().stream()
                .map(ConcertResponse::of).collect(Collectors.toList())
        )
    }
}
