package kr.hanghae.deploy.domain

import com.fasterxml.uuid.Generators
import jakarta.persistence.*
import kr.hanghae.deploy.domain.BaseEntity
import lombok.EqualsAndHashCode
import java.math.BigInteger

enum class ConcertStatus {
    AVAILABLE,
    UNAVAILABLE,
}

@Entity
@Table
@EqualsAndHashCode(callSuper = false)
class Concert(
    number: String,
    bookableDates: MutableList<BookableDate> = mutableListOf(),
    name: String,
    status: ConcertStatus = ConcertStatus.AVAILABLE
) : BaseEntity() {

    @OneToMany(mappedBy = "concert", cascade = [CascadeType.ALL], orphanRemoval = true)
    var bookableDates: MutableList<BookableDate> = bookableDates
        protected set

    val number: String = number

    val name: String = name

    @Enumerated(EnumType.STRING)
    var status: ConcertStatus = status

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateBookableDates(bookableDates: MutableList<BookableDate>) {
        this.bookableDates = bookableDates
    }
}
