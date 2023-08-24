package kr.dohoonkim.blog.restapi.common.entity;

import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class UuidPrimaryKeyEntity{
  @Id
  var id : UUID = UUID.randomUUID()

  @CreatedDate
  @Column(name="created_at", updatable = false, nullable = false)
  var createdAt : LocalDateTime = LocalDateTime.now()

  @LastModifiedDate
  @Column(name = "updated_at")
  var updatedAt : LocalDateTime = LocalDateTime.now()
    protected set;

  @PreUpdate
  fun updateLastModifiedDate() {
    this.updatedAt = LocalDateTime.now();
  }

}
