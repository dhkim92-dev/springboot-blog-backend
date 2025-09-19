package kr.dohoonkim.blog.restapi.stash.security.jwt

import com.auth0.jwt.interfaces.DecodedJWT
import kr.dohoonkim.blog.restapi.domain.member.Member
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.security.Principal
import java.util.UUID

/**
 * 이 클래스는 Access Token으로부터 파싱된 [JwtClaims] 기반으로 [Authentication] interface를 구현한다.
 * 이 객체가 정상 생성되었다는 것은 인증이 정상적으로 완료되었다는 것을 의미하기 때문에 [Authentication.isAuthenticated] 함수는 무조건 true를 반환한다.
 * [Authentication.getCredentials] 의 경우 의미 없는 값이기 때문에 ""를 반환한다.
 * @property id 사용자 ID
 * @property email 사용자 email
 * @property nickname 사용자 nickname
 * @property roles 사용자 권한 목록
 * @property isActivated 계정 활성화 여부
 */
class JwtAuthentication(
    val id: UUID,
    val email: String,
    val nickname: String,
    val roles: MutableCollection<out GrantedAuthority>,
    val isActivated: Boolean
): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return nickname
    }

    override fun isAccountNonExpired(): Boolean {
        return isActivated
    }

    override fun isEnabled(): Boolean {
        return isActivated
    }

    override fun isAccountNonLocked(): Boolean {
        return isActivated
    }

    override fun isCredentialsNonExpired(): Boolean {
        return isActivated
    }

    companion object {

        fun fromMember(member: Member): JwtAuthentication {
            return JwtAuthentication(
                id = member.id,
                nickname = member.nickname,
                email = member.email,
                roles = mutableListOf( SimpleGrantedAuthority(member.role.rolename) ),
                isActivated = member.isActivated
            )
        }

        fun fromDecodedJwt(jwt: DecodedJWT): JwtAuthentication {
            val memberId: UUID = UUID.fromString(jwt.subject)
            val email: String = jwt.getClaim("email").asString()
            val nickname: String = jwt.getClaim("nickname").asString()
            val roles = jwt.getClaim("roles")
                .asArray(String::class.java)
                .map { rolename -> SimpleGrantedAuthority(rolename) }
                .toMutableList()
            val isActivated = jwt.getClaim("isActivated").asBoolean()
            return JwtAuthentication(memberId, email, nickname, roles, isActivated)
        }
    }
}