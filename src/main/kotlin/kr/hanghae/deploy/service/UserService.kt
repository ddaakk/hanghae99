package kr.hanghae.deploy.service

import kr.hanghae.deploy.annotation.DistributedLock
import kr.hanghae.deploy.component.UserManager
import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.dto.user.ChargeBalanceServiceRequest
import kr.hanghae.deploy.dto.user.GenerateTokenServiceResponse
import kr.hanghae.deploy.dto.user.GetBalanceServiceRequest
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class UserService(
    private val userReader: UserReader,
    private val userManager: UserManager,
    private val redisService: RedisService,
) {

    val logger = KotlinLogging.logger {}

    @Transactional
    fun generateToken(): GenerateTokenServiceResponse {

        val queueSize = redisService.getQueueSize()
            ?: throw RuntimeException("대기열을 확인할 수 없습니다. 잠시 후 재시도 해주세요.")

        if (queueSize > 100) {
            throw RuntimeException("대기열이 가득찼습니다. 잠시 후 재시도 해주세요.")
        }

        val user = User()
        redisService.registerQueue(user.uuid)
        val order = redisService.getQueueOrder(user.uuid)?.toInt() ?: 0
        userManager.saveUser(user)

        return GenerateTokenServiceResponse.from(uuid = user.uuid, waiting = order, remainTime = order * 100)
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
