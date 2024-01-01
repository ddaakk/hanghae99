package kr.hanghae.deploy.component

import kr.hanghae.deploy.annotation.DistributedLock
import kr.hanghae.deploy.util.CustomSpringELParser
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.core.annotation.Order
import java.util.concurrent.TimeUnit

@Aspect
@Component
@Order(1)
class DistributedLockAop(
    private val redissonClient: RedissonClient,
    private val aopForTransaction: AopForTransaction,
) {

    companion object {
        private const val REDISSON_LOCK_PREFIX = "LOCK:"
    }

    @Around("@annotation(kr.hanghae.deploy.annotation.DistributedLock)")
    fun lock(joinPoint: ProceedingJoinPoint): Any {
        val signature = joinPoint.signature as MethodSignature
        val method = signature.method
        val distributedLock = method.getAnnotation(DistributedLock::class.java)

        val key = REDISSON_LOCK_PREFIX +
            CustomSpringELParser.getDynamicValue(
                signature.parameterNames,
                joinPoint.args,
                distributedLock.key
            )
//        log.info("lock on [method:{}] [key:{}]", method, key)

        val rLock: RLock = redissonClient.getLock(key)
        val lockName = rLock.name
        try {
            val available = rLock.tryLock(
                distributedLock.waitTime,
                distributedLock.leaseTime,
                distributedLock.timeUnit
            )
            if (!available) {
                throw RuntimeException("락을 현재 사용할 수 없습니다.")
            }

            return aopForTransaction.proceed(joinPoint)

        } catch (e: InterruptedException) {
            // 락을 얻으려고 시도하다가 인터럽트를 받았을 때 발생
            throw RuntimeException("락 획득에 실패하였습니다.")
        } finally {
            try {
                rLock.unlock()
//                log.info("unlock complete [Lock:{}] ", lockName)
            } catch (e: IllegalMonitorStateException) {
                // 락이 이미 종료되었을때 발생
//                log.info("Redisson Lock Already Unlocked")
            }
        }
    }
}
