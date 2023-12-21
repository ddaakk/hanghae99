package kr.hanghae.deploy.domain.seat

import io.kotest.common.ExperimentalKotest
import io.kotest.core.extensions.Extension
import io.kotest.core.names.TestName
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.spec.style.scopes.DescribeSpecContainerScope
import io.kotest.extensions.spring.SpringExtension
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kr.hanghae.deploy.domain.bookabledate.BookableDate
import kr.hanghae.deploy.domain.bookabledate.BookableDateRepository
import kr.hanghae.deploy.domain.bookabledate.BookableDateRepositoryImpl
import kr.hanghae.deploy.domain.seat.Seat
import kr.hanghae.deploy.domain.user.User
import kr.hanghae.deploy.domain.user.UserRepositoryImpl
import kr.hanghae.deploy.txContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
internal class SeatRepositoryTest(
    private val seatRepositoryImpl: SeatRepositoryImpl,
    private val bookableDateRepositoryImpl: BookableDateRepositoryImpl,
    private val userRepositoryImpl: UserRepositoryImpl,
) : DescribeSpec({

    describe("findAllByDate") {
        txContext("예약 가능한 날짜 정보가 주어지면") {

            val bookableDate = BookableDate(date = "2023-12-01")
            val seats = mutableListOf(
                Seat(bookableDate = bookableDate, orders = 1),
                Seat(bookableDate = bookableDate, orders = 2),
            )
            bookableDate.updateSeats(seats = seats)
            bookableDateRepositoryImpl.saveAndFlush(bookableDate)
            seatRepositoryImpl.saveAllAndFlush(seats)

            it("해당 날짜의 좌석 정보들을 반환한다") {
                val seats = seatRepositoryImpl.findAllByDate(date = "2023-12-01")

                seats shouldHaveSize 2
                seats[0].bookableDate.date shouldBe "2023-12-01"
                seats[1].bookableDate.date shouldBe "2023-12-01"
                seats.map { it.orders }.shouldContainExactlyInAnyOrder(listOf(1, 2))
            }
        }
    }

    describe("findByOrderAndDate") {
        txContext("예약을 원하는 예약 가능 날짜와 좌석 번호들이 주어지면") {

            val user = User(uuid = "uuid")
            userRepositoryImpl.saveAndFlush(user)

            val bookableDate = BookableDate(date = "2023-12-01")
            val seats = mutableListOf(
                Seat(bookableDate = bookableDate, orders = 1, user = user),
                Seat(bookableDate = bookableDate, orders = 2, user = user),
            )
            bookableDate.updateSeats(seats = seats)
            bookableDateRepositoryImpl.saveAndFlush(bookableDate)
            seatRepositoryImpl.saveAllAndFlush(seats)

            it("해당 날짜의 좌석 정보들을 반환한다") {
                val seats = seatRepositoryImpl.findByOrderAndDate(seats = listOf(1, 2), date = "2023-12-01")
                // verify
                seats shouldHaveSize 2
                seats[0].bookableDate.date shouldBe "2023-12-01"
                seats[0].user!!.uuid shouldBe "uuid"
                seats[1].bookableDate.date shouldBe "2023-12-01"
                seats[1].user!!.uuid shouldBe "uuid"
                seats.map { it.orders }.shouldContainExactlyInAnyOrder(listOf(1, 2))
            }
        }
    }

})
