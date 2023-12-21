package kr.hanghae.deploy.dto.emitter

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

data class Emitter(
    var emitter: SseEmitter,
    val waitingOrder: Int,
) {
}
