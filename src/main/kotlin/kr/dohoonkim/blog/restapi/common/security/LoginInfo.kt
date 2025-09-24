package kr.dohoonkim.blog.restapi.common.security

import kr.dohoonkim.blog.restapi.domain.member.Role
import java.util.UUID

/**
 * 현재 로그인한 사용자 정보
 * - memberId: UUID
 * - nickname: String
 * - roles: List<Role>
 */
class LoginInfo(
    val memberId: UUID,
    val nickname: String,
    val roles: List<Role>
) {
}