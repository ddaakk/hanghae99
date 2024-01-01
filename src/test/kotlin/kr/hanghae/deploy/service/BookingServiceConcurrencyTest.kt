package kr.hanghae.deploy.service

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kr.hanghae.deploy.DatabaseCleanUpExecutor
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.dto.booking.BookingServiceRequest
import kr.hanghae.deploy.repository.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import java.math.BigDecimal
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@SpringBootTest
internal class BookingServiceConcurrencyTest(
    private val jdbcTemplate: JdbcTemplate,
    private val concertRepositoryImpl: ConcertRepositoryImpl,
    private val userRepositoryImpl: UserRepositoryImpl,
    private val bookableDateRepositoryImpl: BookableDateRepositoryImpl,
    private val seatRepositoryImpl: SeatRepositoryImpl,
    private val bookingRepositoryImpl: BookingRepositoryImpl,
    private val bookingService: BookingService,
) : DescribeSpec() {
    init {
        describe("requestBooking") {
            context("동시에 다수의 인원이 예약을 시도할 시") {

                userRepositoryImpl.save(User(uuid = "uuid", balance = BigDecimal(100000)))
                val concert = Concert(name = "고척돔", number = "1234")
                concertRepositoryImpl.save(concert)
                val bookableDate = BookableDate(date = LocalDate.now(), concert = concert)
                bookableDateRepositoryImpl.save(bookableDate)
                concert.updateBookableDates(mutableListOf(bookableDate))
                seatRepositoryImpl.saveAll(
                    listOf(
                        Seat(bookableDateId = 1, number = 1, grade = "A", price = 5000),
                        Seat(bookableDateId = 1, number = 2, grade = "B", price = 3000),
                        Seat(bookableDateId = 1, number = 3, grade = "C", price = 1000),
                    )
                )

                val numberOfThreads = 10
                val service: ExecutorService = Executors.newFixedThreadPool(numberOfThreads)
                val latch = CountDownLatch(numberOfThreads)

                repeat(numberOfThreads) {
                    service.execute {
                        try {
                            bookingService.requestBooking(
                                BookingServiceRequest.toService(
                                    concertNumber = "1234",
                                    date = LocalDate.now(),
                                    seatNumbers = listOf(1, 2, 3),
                                    uuid = "uuid"
                                )
                            )
                        } finally {
                            latch.countDown()
                        }
                    }
                }

                latch.await()

                it("한 명만 예약에 성공한다") {
                    bookingRepositoryImpl.findAll().size shouldBe 1
                }
            }
        }
    }

    override fun listeners(): List<TestListener> = listOf(DatabaseCleanUpExecutor(jdbcTemplate))
}
