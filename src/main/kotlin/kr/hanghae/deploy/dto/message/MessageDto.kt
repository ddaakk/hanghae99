package kr.hanghae.deploy.dto.message

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

data class MessageDto @JsonCreator constructor(
    @JsonProperty("title") val title: String,
    @JsonProperty("content") val content: String,
    @JsonProperty("waitingOrder") val waitingOrder: Int,
)
