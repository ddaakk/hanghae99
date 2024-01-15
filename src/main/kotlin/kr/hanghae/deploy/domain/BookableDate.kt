package kr.hanghae.deploy.domain

import jakarta.persistence.*
import lombok.EqualsAndHashCode
import java.time.LocalDate

@Entity
@Table
@EqualsAndHashCode(callSuper = false)
class BookableDate(
    date: LocalDate,
    concert: Concert,
) : BaseEntity() {

    @ManyToOne(fetch = FetchType.LAZY)
    val concert: Concert = concert

    @Column(nullable = false)
    val date: LocalDate = date

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
