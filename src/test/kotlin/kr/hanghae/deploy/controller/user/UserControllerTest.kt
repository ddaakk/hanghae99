package kr.hanghae.deploy.controller.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import kr.hanghae.deploy.dto.user.response.ApplyTokenResponse
import kr.hanghae.deploy.service.user.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath


@WebMvcTest
class UserControllerTest @Autowired constructor(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,

) {

    @MockkBean
    lateinit var userService: UserService

    @Test
    fun `다른 API를 호출하기 위한 신규 토큰을 생성한다`() {
        // given
        every { userService.applyToken() } returns ApplyTokenResponse("1", "2", "3")

        // when

        // then
        mockMvc.perform(post("/api/v1/user/token"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.code").value("200"))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("OK"));
    }

}
