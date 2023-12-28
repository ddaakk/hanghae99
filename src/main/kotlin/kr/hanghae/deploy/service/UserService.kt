package kr.hanghae.deploy.service

import kr.hanghae.deploy.component.UserManager
import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.repository.EmitterRepository
import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.repository.UserRepositoryImpl
import kr.hanghae.deploy.dto.message.MessageDto
import kr.hanghae.deploy.dto.user.ChargeBalanceServiceRequest
import kr.hanghae.deploy.dto.user.GetBalanceServiceRequest
import kr.hanghae.deploy.dto.user.response.GenerateTokenResponse
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException
import java.util.*


@Service
class UserService(
    private val userReader: UserReader,
    private val userManager: UserManager,
    private val redisService: RedisService,
) {

    val logger = KotlinLogging.logger {}

    @Transactional
    fun generateToken(): User {
        val user = User()

        redisService.addQueue(user.uuid)

        return userManager.saveUser()
    }

    @Transactional
    fun chargeBalance(request: ChargeBalanceServiceRequest): User {
        val (balance, uuid) = request
        val user = userReader.getByUUID(uuid)
        user.chargeBalance(balance)
        return user
    }

    @Transactional(readOnly = true)
    fun getBalance(request: GetBalanceServiceRequest): User {
        val (uuid) = request
        return userReader.getByUUID(uuid)
    }
}
