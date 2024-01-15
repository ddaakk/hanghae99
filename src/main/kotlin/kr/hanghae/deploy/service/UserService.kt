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

        val completeSize = redisService.getZSetSize(Key.COMPLETE.toString()) ?: 0

        val uuid = Generators.timeBasedGenerator().generate().toString()
        val user = User(uuid)
        var order = 0

        if (completeSize > 100) {
            redisService.addZSet(Key.WAITING.toString(), "waiting${user.uuid}")
            order = redisService.getZSetRank(Key.WAITING.toString(), "waiting${user.uuid}")?.toInt() ?: 0

            logger.info {
                "새로운 사용자가 등록하였습니다. 완료 대기열이 가득차서 대기열에 등록합니다. UUID: ${user.uuid}, " +
                    "대기 순서: $order, 남은 시간: 최대 ${order * redisService.expireTime.toMinutes()}분"
            }
        } else {
            redisService.addZSetIfAbsent(Key.COMPLETE.toString(), "complete$uuid")

            logger.info {
                "새로운 사용자가 등록되었습니다. 완료 대기열에 등록합니다. UUID: ${user.uuid}, " +
                    "대기 순서: $order, 남은 시간: 최대 ${order * redisService.expireTime.toMinutes()}분"
            }
        }

        userManager.saveUser(user)

        return GenerateTokenServiceResponse.from(uuid = user.uuid, waiting = order, remainTime = order)
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
