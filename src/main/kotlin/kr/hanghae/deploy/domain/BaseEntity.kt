package kr.hanghae.deploy.domain

import jakarta.persistence.*
import kr.hanghae.deploy.annotation.AllOpen
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.time.OffsetDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@AllOpen
abstract class BaseEntity {

    lateinit var createdAt: LocalDateTime

    lateinit var updatedAt: LocalDateTime

    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
        updatedAt = LocalDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }
}
