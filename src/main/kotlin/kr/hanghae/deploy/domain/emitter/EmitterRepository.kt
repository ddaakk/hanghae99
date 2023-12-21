package kr.hanghae.deploy.domain.emitter

import kr.hanghae.deploy.dto.emitter.Emitter
import org.springframework.stereotype.Repository
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors

@Repository
class EmitterRepository {
    private val emitters: MutableMap<String, Emitter> = ConcurrentHashMap()
    private val eventCache: MutableMap<String, Any> = ConcurrentHashMap()
    private val finishEmitters: MutableMap<String, Boolean> = ConcurrentHashMap()

    fun getFinishEmitters(): MutableMap<String, Boolean> {
        return finishEmitters;
    }
    fun save(emitterId: String, sseEmitter: SseEmitter, waitingOrder: Int): Emitter {
        val emitter = Emitter(sseEmitter, waitingOrder)
        emitters[emitterId] = emitter
        return emitter
    }

    fun saveFinish(emitterId: String) {
        finishEmitters[emitterId] = true
    }

    fun saveEventCache(eventCacheId: String, event: Any) {
        eventCache[eventCacheId] = event
    }

    fun deleteById(emitterId: String) {
        emitters.remove(emitterId)
    }

    fun deleteFinishById(emitterId: String) {
        finishEmitters.remove(finishEmitters.filterKeys { it.startsWith(emitterId) }.keys.first())
    }

    fun findAllEmitterByUuid(uuid: String): Map<String, Emitter> {
        return emitters.entries
            .stream()
            .filter { entry -> entry.key.startsWith(uuid) }
            .collect(Collectors.toMap({ it.key }, { it.value }))
    }

    fun findAllEventCacheWithUuid(uuid: String): Map<String, Any> {
        return eventCache.entries
            .stream()
            .filter { entry -> entry.key.startsWith(uuid) }
            .collect(Collectors.toMap({ it.key }, { it.value }))
    }

    fun findAllEmitterWithoutSelf(uuid: String): Map<String, Emitter> {
        return emitters.entries
            .stream()
            .filter { entry -> !entry.key.startsWith(uuid) }
            .collect(Collectors.toMap({ it.key }, { it.value }))
    }

    fun findAllEmitter(): Map<String, Emitter> {
        return emitters.entries.stream()
            .collect(Collectors.toMap({ it.key }, { it.value }))
    }
}
