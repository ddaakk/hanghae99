package kr.hanghae.deploy.domain.user

import jakarta.persistence.*
import kr.hanghae.deploy.domain.booking.Booking
import kr.hanghae.deploy.domain.common.BaseEntity
import kr.hanghae.deploy.domain.seat.Seat

@Entity
class User (
    val uuid: String,

    val balance: Long = 0L,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val booking: MutableList<Booking> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    ) : BaseEntity() {
}
