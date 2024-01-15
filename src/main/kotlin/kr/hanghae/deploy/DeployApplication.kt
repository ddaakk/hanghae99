package kr.hanghae.deploy

import jakarta.annotation.PostConstruct
import kr.hanghae.deploy.service.RedisService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableAsync
class DeployApplication(
    private val redisService: RedisService,
) {

    @PostConstruct
    fun bootstrapRedis() {
        redisService.flushAll()
        redisService.addValue("counter", "0")
        redisService.addValue("throughput", "10000")
        redisService.addValue("cycleInterval", "60000")
    }
}

fun main(args: Array<String>) {
    runApplication<DeployApplication>(*args)
}
