package kr.dohoonkim.blog.restapi.security.jwt

import com.auth0.jwt.interfaces.DecodedJWT
import kr.dohoonkim.blog.restapi.application.authentication.vo.JwtClaims
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.UUID

/**
 * 이 클래스는 Access Token으로부터 파싱된 [JwtClaims] 기반으로 [Authentication] interface를 구현한다.
 * 이 객체가 정상 생성되었다는 것은 인증이 정상적으로 완료되었다는 것을 의미하기 때문에 [Authentication.isAuthenticated] 함수는 무조건 true를 반환한다.
 * [Authentication.getCredentials] 의 경우 의미 없는 값이기 때문에 null을 반환한다.
 */
class JwtAuthentication(
    val id: UUID,
    val email: String,
    val nickname: String,
    val roles: MutableCollection<out GrantedAuthority>,
    val isActivated: Boolean
) : Authentication {

    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any {
        val roles: Array<String> = roles.map { role -> role.authority }.toTypedArray()
        return JwtClaims(id, email, nickname, roles, isActivated)
    }

    override fun getName(): String {
        return nickname
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles
    }

    override fun getDetails(): Any {
        return JwtClaims(id, email, nickname, roles.map { role -> role.authority }.toTypedArray(), isActivated)
    }

    override fun isAuthenticated(): Boolean {
        return true
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {}

    companion object {
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