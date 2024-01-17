package kr.hanghae.deploy.repository

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.matchers.shouldBe
import kr.hanghae.deploy.DatabaseCleanUpExecutor
import kr.hanghae.deploy.domain.*
import kr.hanghae.deploy.txContext
import kr.hanghae.deploy.repository.UserRepositoryImpl
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime

@DataJpaTest
internal class BookingRepositoryTest(
    private val jdbcTemplate: JdbcTemplate,
    private val seatRepositoryImpl: SeatRepositoryImpl,
    private val bookingRepositoryImpl: BookingRepositoryImpl,
) : DescribeSpec() {

    override suspend fun beforeEach(testCase: TestCase) {
        val booking = Booking(userId = 1, date = LocalDate.now(), number = "1234")
        val seats = mutableListOf(
            Seat(bookableDateId = 1, number = 1, grade = "A"),
            Seat(bookableDateId = 1, number = 2, grade = "A"),
        )
        booking.updateSeats(seats)
        bookingRepositoryImpl.save(booking)
        seatRepositoryImpl.saveAll(seats)
    }

    init {
        describe("findByBookingNumber") {
            txContext("예약 번호가 주어지면") {
                it("해당 예약 정보를 반환한다") {
                    val booking = bookingRepositoryImpl.findByBookingNumber(bookingNumber = "1234", userId = 1)
                    booking!!.userId shouldBe 1
                    booking.date shouldBe LocalDate.now()
                    booking.status shouldBe BookingStatus.BOOKING
                    booking.seats.size shouldBe 2
                    booking.number shouldBe "1234"
                }
            }
        }

        describe("findByUpdatedTime") {
            txContext("사용자 UUID 정보가 주어지면") {
                it("해당 사용자가 존재하는지 확인한다") {
                    val bookings = bookingRepositoryImpl.findByUpdatedTime(LocalDateTime.now())
                    bookings.size shouldBe 1
                    bookings[0].userId shouldBe 1
                    bookings[0].date shouldBe LocalDate.now()
                    bookings[0].status shouldBe BookingStatus.BOOKING
                    bookings[0].seats.size shouldBe 2
                    bookings[0].number shouldBe "1234"
                }
            }
        }

        describe("findByBookingNumberWithLock") {
            txContext("예약 번호가 주어지면") {
                it("해당 예약 정보를 반환한다") {
                    val booking = bookingRepositoryImpl.findByBookingNumberWithLock(bookingNumber = "1234", userId = 1)
                    booking!!.userId shouldBe 1
                    booking.date shouldBe LocalDate.now()
                    booking.status shouldBe BookingStatus.BOOKING
                    booking.seats.size shouldBe 2
                    booking.number shouldBe "1234"
                }
            }
        }
    }

    override fun listeners(): List<TestListener> = listOf(DatabaseCleanUpExecutor(jdbcTemplate))
}
