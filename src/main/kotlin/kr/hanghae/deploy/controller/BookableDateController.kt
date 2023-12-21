package kr.hanghae.deploy.controller

import kr.hanghae.deploy.dto.ApiResponse
import kr.hanghae.deploy.dto.bookabledate.response.BookableDateResponse
import kr.hanghae.deploy.service.BookableDateService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class BookableDateController(
    private val bookableDateService: BookableDateService
) {

    @GetMapping("/api/v1/bookabledate")
    fun getBookableDates(): ApiResponse<List<BookableDateResponse>> {
        return ApiResponse.ok(data = bookableDateService.getBookableDates())
    }
}
