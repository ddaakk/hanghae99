package kr.hanghae.deploy.domain.bookabledate

import jakarta.persistence.*
import kr.hanghae.deploy.domain.common.BaseEntity
import kr.hanghae.deploy.domain.seat.Seat
import java.time.LocalDate

@Entity
class BookableDate(
    @OneToMany(mappedBy = "bookableDate", cascade = [CascadeType.ALL], orphanRemoval = true)
    val seats: MutableList<Seat> = mutableListOf(),

    val date: LocalDate,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
) : BaseEntity() {

}
