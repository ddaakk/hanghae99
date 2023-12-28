package kr.hanghae.deploy.repository

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kr.hanghae.deploy.domain.BookableDate
import kr.hanghae.deploy.domain.Concert
import kr.hanghae.deploy.domain.Seat
import kr.hanghae.deploy.domain.User
import kr.hanghae.deploy.repository.BookableDateRepositoryImpl
import kr.hanghae.deploy.repository.UserRepositoryImpl
import kr.hanghae.deploy.txContext
import kr.hanghae.deploy.repository.SeatRepositoryImpl
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.transaction.annotation.Transactional

@DataJpaTest
@Transactional
internal class SeatRepositoryTest(
    private val seatRepositoryImpl: SeatRepositoryImpl,
    private val bookableDateRepositoryImpl: BookableDateRepositoryImpl,
    private val userRepositoryImpl: UserRepositoryImpl,
    private val concertRepositoryImpl: ConcertRepositoryImpl,
) : DescribeSpec({

    describe("findAllByDate") {
        txContext("예약 가능한 날짜 정보가 주어지면") {

            val concert = Concert("고척돔")
            concertRepositoryImpl.saveAndFlush(concert)

            val bookableDate = BookableDate(date = "2023-12-01")
            val seats = mutableListOf(
                Seat(bookableDate = bookableDate, number = 1, concert = concert, grade = "A"),
                Seat(bookableDate = bookableDate, number = 2, concert = concert, grade = "A"),
            )
            bookableDate.updateSeats(seats = seats)
            bookableDateRepositoryImpl.saveAndFlush(bookableDate)
            seatRepositoryImpl.saveAllAndFlush(seats)

            it("해당 날짜의 좌석 정보들을 반환한다") {
                val seats = seatRepositoryImpl.findAllByDate(date = "2023-12-01")

                seats shouldHaveSize 2
                seats[0].bookableDate.date shouldBe "2023-12-01"
                seats[1].bookableDate.date shouldBe "2023-12-01"
                seats.map { it.number }.shouldContainExactlyInAnyOrder(listOf(1, 2))
            }
        }
    }

    describe("findByOrderAndDate") {
        txContext("예약을 원하는 예약 가능 날짜와 좌석 번호들이 주어지면") {

            val user = User(uuid = "uuid")
            userRepositoryImpl.saveAndFlush(user)

            val concert = Concert("고척돔")
            concertRepositoryImpl.saveAndFlush(concert)

            val bookableDate = BookableDate(date = "2023-12-01")
            val seats = mutableListOf(
                Seat(bookableDate = bookableDate, number = 1, concert = concert, grade = "A"),
                Seat(bookableDate = bookableDate, number = 2, concert = concert, grade = "A"),
            )
            bookableDate.updateSeats(seats = seats)
            bookableDateRepositoryImpl.saveAndFlush(bookableDate)
            seatRepositoryImpl.saveAllAndFlush(seats)

            it("해당 날짜의 좌석 정보들을 반환한다") {
                val seats = seatRepositoryImpl.findByOrderAndDate(seatNumbers = listOf(1, 2), date = "2023-12-01")
                // verify
                seats shouldHaveSize 2
                seats[0].bookableDate.date shouldBe "2023-12-01"
                seats[1].bookableDate.date shouldBe "2023-12-01"
                seats.map { it.number }.shouldContainExactlyInAnyOrder(listOf(1, 2))
            }
        }
    }

})
