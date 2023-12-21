package kr.hanghae.deploy

import kr.hanghae.deploy.controller.UserController
import kr.hanghae.deploy.service.MessageService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

//@SpringBootTest
//class Concurrency @Autowired constructor(
//    private val userController: UserController,
//) {
//
//    @Test
//    fun testCounterWithConcurrency() {
//        val numberOfThreads = 1000
//        val service: ExecutorService = Executors.newFixedThreadPool(10)
//        val latch = CountDownLatch(numberOfThreads)
//
//        repeat(numberOfThreads) {
//            service.execute {
//                userController.generateToken("")
//                latch.countDown()
//            }
//        }
//
//        Thread.sleep(10000)
//
//        latch.await()
//    }
//}
