package kr.dohoonkim.blog.restapi.security.jwt

import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import org.springframework.security.authentication.AbstractAuthenticationToken

/**
 * JwtAuthenticationProvider 에서 다룰 JwtAuthenticationToken
 * @property _principal : 인증전 JWT String, 인증 후 JwtAuthentication
 */

class JwtAuthenticationToken(private val _principal : Any?)
: AbstractAuthenticationToken(null) {

    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any? {
        return _principal
    }
//
//    companion object {
//        fun fromCustomUserDetails(details : CustomUserDetails) : JwtAuthenticationToken {
//            return JwtAuthenticationToken(
//                    JwtAuthentication(id=details.memberId,
//                            email=details.email,
//                            nickname=details.nickname,
//                            roles=details.authorities,
//                            isActivated = details.isActivated)
//            )
//        }
//    }
}