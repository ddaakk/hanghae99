package kr.hanghae.deploy.domain

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table
class BookableDate(
    date: LocalDate,
    concert: Concert,
) : BaseEntity() {

    @ManyToOne(fetch = FetchType.LAZY)
    var concert: Concert = concert
        protected set

    @Column(nullable = false)
    var date: LocalDate = date
        protected set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
