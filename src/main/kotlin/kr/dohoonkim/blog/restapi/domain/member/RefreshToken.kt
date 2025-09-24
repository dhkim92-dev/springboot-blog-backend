package kr.dohoonkim.blog.restapi.domain.member

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import kr.dohoonkim.blog.restapi.common.generator.UUIDGenerator
import kr.dohoonkim.blog.restapi.domain.BaseEntity
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UuidGenerator
import org.springframework.stereotype.Indexed
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

/**
 * 사용자 리프레시 토큰 엔티티
 * 발급된 리프레시 토큰을 저장하는 용도
 * 리프레시 토큰을 DB에 저장하는 이유는 보안 및 관리 목적
 * - 보안: 리프레시 토큰이 탈취되었을 때, 이를 무효화할 수 있음
 * - 관리: 사용자가 로그아웃하거나 토큰을 갱신할 때,
 */
@Entity
@Table(name = "refresh_token")
class RefreshToken(
    @Id
    @GeneratedValue
    @UuidGenerator(algorithm = UUIDGenerator::class)
    val id: UUID? = null,
    member: Member = Member(),
    expireAt: Instant = Instant.now().plusSeconds(60 * 60 * 24 * 14), // 기본 만료 기간 7일
    token: String = ""
): BaseEntity() {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member = member

    @Column(nullable = false)
    var token: String = token

    @Column
    val expireAt: Instant = expireAt

    fun isExpired(): Boolean {
        return Instant.now().isAfter(expireAt)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RefreshToken) return false
        return token == other.token
    }

    override fun hashCode(): Int {
        return token.hashCode()
    }
}