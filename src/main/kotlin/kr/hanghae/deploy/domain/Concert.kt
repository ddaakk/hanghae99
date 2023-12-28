package kr.hanghae.deploy.domain

import jakarta.persistence.*
import kr.hanghae.deploy.domain.BaseEntity

@Entity
@Table
class Concert(
    name: String,
) : BaseEntity() {
    var name: String = name
        protected set

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}
