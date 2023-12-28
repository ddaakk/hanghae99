package kr.hanghae.deploy.domain

import jakarta.persistence.*

@Entity
@Table
class BookableDate(
    seats: MutableList<Seat> = mutableListOf(),
    date: String,
) : BaseEntity() {
    @OneToMany(mappedBy = "bookableDate", cascade = [CascadeType.ALL], orphanRemoval = true)
    var seats: MutableList<Seat> = seats
        protected set

    @Column(nullable = false) // length 지정
    var date: String = date
        protected set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateSeats(seats: MutableList<Seat>) {
        this.seats = seats
    }
}
