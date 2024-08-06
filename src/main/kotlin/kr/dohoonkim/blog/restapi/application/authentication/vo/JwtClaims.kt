package kr.dohoonkim.blog.restapi.application.authentication.vo

import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import java.util.UUID

/**
 * JwtClaims
 * @property id 사용자 ID
 * @property email 사용자 email
 * @property nickname 사용자 nickname
 * @property roles 사용자 권한 리스트
 * @property isActivated 사용자 이메일 인증 여부
 * TODO 추후 UserDetails 인터페이스를 구현할 것
 */
data class JwtClaims(
    val id: UUID,
    val email: String,
    val nickname: String,
    val roles: Array<String>,
    val isActivated: Boolean
) {

    companion object {
        fun fromCustomUserDetails(detail: CustomUserDetails): JwtClaims {
            return JwtClaims(
                detail.memberId,
                detail.email,
                detail.nickname,
                arrayOf(detail.role.rolename),
                detail.isActivated
            )
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JwtClaims

        if (id != other.id) return false
        if (email != other.email) return false
        if (nickname != other.nickname) return false
        if (!roles.contentEquals(other.roles)) return false
        if (isActivated != other.isActivated) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + nickname.hashCode()
        result = 31 * result + roles.contentHashCode()
        result = 31 * result + isActivated.hashCode()
        return result
    }
}