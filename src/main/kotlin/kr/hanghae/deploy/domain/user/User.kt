package kr.hanghae.deploy.domain.user

import jakarta.persistence.*
import kr.hanghae.deploy.domain.booking.Booking
import kr.hanghae.deploy.domain.common.BaseEntity
import kr.hanghae.deploy.domain.seat.Seat
import java.util.*

@Entity
@Table(name = "USERS")
class User(
    val token: String = UUID.randomUUID().toString(),

    val balance: Long = 0L,

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    val booking: MutableList<Booking> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    ) : BaseEntity() {
}
