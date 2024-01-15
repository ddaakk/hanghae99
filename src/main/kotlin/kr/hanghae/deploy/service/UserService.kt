package kr.hanghae.deploy.service

import com.fasterxml.uuid.Generators
import kr.hanghae.deploy.component.UserManager
import kr.hanghae.deploy.component.UserReader
import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.dto.user.ChargeBalanceServiceRequest
import kr.hanghae.deploy.dto.user.GenerateTokenServiceResponse
import kr.hanghae.deploy.dto.user.GetBalanceServiceRequest
import kr.hanghae.deploy.repository.UserRepositoryImpl
import mu.KotlinLogging
import org.springframework.orm.ObjectOptimisticLockingFailureException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Duration
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

@Service
class UserService(
    private val userReader: UserReader,
    private val userManager: UserManager,
    private val redisService: RedisService,
    private val userRepositoryImpl: UserRepositoryImpl,
) {

    @Transactional
    fun generateToken(): GenerateTokenServiceResponse {

        val uuid = Generators.timeBasedGenerator().generate().toString()
        val user = User(uuid)

        redisService.addValue(key = uuid, value = redisService.calculateEntranceTime(uuid))

        userManager.saveUser(user)

        val counter = redisService.getCounter()
        val remainTime = redisService.calculateRemainTime().toInt()

        logger.info {
            "새로운 사용자가 등록되었습니다. 대기열에 등록합니다. UUID: ${user.uuid}, " +
                "대기 순서: ${redisService.getCounter()}, 남은 시간: 최대 ${redisService.calculateRemainTime()}분"
        }

        return GenerateTokenServiceResponse.from(uuid = user.uuid, waiting = counter, remainTime = remainTime)
    }

    @Transactional
    fun chargeBalance(request: ChargeBalanceServiceRequest): User {
        try {
            val (balance, uuid) = request
            val user = userReader.getByUUIDWithLock(uuid)
            user.chargeBalance(balance)
            userRepositoryImpl.saveAndFlush(user)
            logger.info {
                "포인트 충전에 성공하였습니다. 잔액: $balance, " +
                    "사용자 아이디: $uuid"
            }
            return user
        } catch (e: ObjectOptimisticLockingFailureException) {
            throw RuntimeException("포인트 충전에 실패하였습니다.")
        }
    }

    @Transactional(readOnly = true)
    fun getBalance(request: GetBalanceServiceRequest): User {
        val (uuid) = request
        return userReader.getByUUID(uuid)
    }
}
