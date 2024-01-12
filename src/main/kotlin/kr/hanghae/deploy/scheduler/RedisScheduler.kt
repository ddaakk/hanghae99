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
//    private val userReader: UserReader,
//    private val redisService: RedisService,
//) {
//    @Scheduled(fixedDelay = 100)
//    @Transactional
//    fun popQueue() {
//        // TODO("ApplicationEvent 사용해서 pubsub 구조로 큐 빼주기")
//        val uuid = redisService.popZSetMin(count = 1, key = Key.WAITING.toString(), type = String::class.java).poll()
////        uuid?.let {
////            val user = userReader.getByUUID(uuid)
////            user.updateWaiting(0)
////        }
//        uuid?.let {
//            redisService.addHash(Key.COMPLETE.toString(), uuid)
//        }
//    }
//}
