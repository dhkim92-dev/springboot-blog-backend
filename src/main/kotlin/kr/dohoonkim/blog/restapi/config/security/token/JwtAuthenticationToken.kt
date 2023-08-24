package kr.dohoonkim.blog.restapi.config.security.jwt

import kr.dohoonkim.blog.restapi.config.security.authentication.JwtAuthentication
import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import org.springframework.security.authentication.AbstractAuthenticationToken

/**
 * JwtAuthenticationProvider 에서 다룰 JwtAuthenticationToken
 * @property _principal : 인증전 JWT String, 인증 후 JwtAuthentication
 * @property _credentials : 여기서는 의미없는 값이다.
 */

class JwtAuthenticationToken(
        principal : Any,
) : AbstractAuthenticationToken(null) {
    var _principal = principal

    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any {
        return this._principal
    }

    companion object {
        fun fromCustomUserDetails(details : CustomUserDetails) : JwtAuthenticationToken {
            return JwtAuthenticationToken(
                    JwtAuthentication(id=details.memberId,
                            email=details.email,
                            nickname=details.nickname,
                            roles=details.authorities,
                            isActivated = details.isActivated)
            )
        }
    }

}