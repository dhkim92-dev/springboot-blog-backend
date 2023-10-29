package kr.dohoonkim.blog.restapi.common.entity

import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PreUpdate
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class DatetimeEntity {
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now()
    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()

    @PreUpdate
    private fun beforeUpdate() {
        this.updatedAt = LocalDateTime.now()
    }
}