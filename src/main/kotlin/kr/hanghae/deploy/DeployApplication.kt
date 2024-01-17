package kr.hanghae.deploy

import jakarta.annotation.PostConstruct
import kr.hanghae.deploy.service.RedisService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableAsync
class DeployApplication

fun main(args: Array<String>) {
    runApplication<DeployApplication>(*args)
}

@PostConstruct
fun bootstrapRedis(redisService: RedisService) {
    redisService.flushAll()
    redisService.addValue("counter", "0")
    redisService.addValue("throughput", "8000")
    redisService.addValue("cycleInterval", "60000")
}
