package kr.hanghae.deploy.controller

import kr.hanghae.deploy.annotation.QueryStringArgResolver
import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.bookabledate.BookableDateServiceRequest
import kr.hanghae.deploy.dto.bookabledate.request.BookableDateRequest
import kr.hanghae.deploy.dto.bookabledate.response.BookableDateResponse
import kr.hanghae.deploy.service.BookableDateService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors

@RestController
class BookableDateController(
    private val bookableDateService: BookableDateService
) {

    @GetMapping("/api/v1/bookabledate")
    fun getBookableDates(@QueryStringArgResolver request: BookableDateRequest): ApiResponse<List<BookableDateResponse>> {
        return ApiResponse.ok(
            data = bookableDateService.getBookableDates(
                BookableDateServiceRequest.toService(concertNumber = request.concertNumber)
            )
                .stream()
                .map(BookableDateResponse::of).collect(Collectors.toList())
        )
    }
}
