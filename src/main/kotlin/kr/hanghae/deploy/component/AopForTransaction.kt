package kr.hanghae.deploy.component

import lombok.extern.log4j.Log4j2
import org.aspectj.lang.ProceedingJoinPoint
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


@Component
class AopForTransaction {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Throws(Throwable::class)
    fun proceed(joinPoint: ProceedingJoinPoint): Any {
        return joinPoint.proceed()
    }
}
