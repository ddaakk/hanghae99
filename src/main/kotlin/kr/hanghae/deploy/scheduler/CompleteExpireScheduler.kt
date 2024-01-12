//package kr.hanghae.deploy.scheduler
//
//import kr.hanghae.deploy.component.UserReader
//import kr.hanghae.deploy.service.Key
//import kr.hanghae.deploy.service.RedisService
//import org.springframework.scheduling.annotation.Scheduled
//import org.springframework.stereotype.Component
//import org.springframework.transaction.annotation.Transactional
//
//@Component
//class RedisScheduler(
//    private val redisService: RedisService,
//) {
//    @Scheduled(fixedDelay = 1000)
//    @Transactional
//    fun popComplete() {
//        val expired = redisService.getZSetRangeByScore(
//            Key.COMPLETE.toString(),
//            0.0,
//            System.currentTimeMillis().toDouble() - redisService.expireTime.toMillis()
//        )
//
//        print(expired)
//    }
//}
