package kr.hanghae.deploy.domain

import com.fasterxml.uuid.Generators
import jakarta.persistence.*
import kr.hanghae.deploy.domain.BaseEntity
import java.math.BigInteger

@Entity
@Table
class Concert(
    number: String = String.format(
        "%040d",
        BigInteger(
            Generators.timeBasedGenerator().generate().toString().replace("-", ""), 16
        )
    ),
    bookableDates: MutableList<BookableDate> = mutableListOf(),
    name: String,
) : BaseEntity() {

    @OneToMany(mappedBy = "concert", cascade = [CascadeType.ALL], orphanRemoval = true)
    var bookableDates: MutableList<BookableDate> = bookableDates
        protected set

    var number: String = number
        protected set

    var name: String = name
        protected set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    fun updateBookableDates(bookableDates: MutableList<BookableDate>) {
        this.bookableDates = bookableDates
    }
}
