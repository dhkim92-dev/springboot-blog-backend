package kr.dohoonkim.blog.restapi.security.jwt

import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

/**
 * JwtAuthentication을 수행하는 Provider
 * @property jwtService JwtService 객체
 */
@Component
class JwtAuthenticationProvider(
    private val jwtService: JwtService
) : AuthenticationProvider {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * JwtAuthenticationToken을 받아 jwtService에 처리를 위임하고 JwtAuthentication을 반환한다
     * @param authentication JWT Access token
     * @throws JWTInvalidException
     * @throws JWTVerificationException
     */
    override fun authenticate(authentication: Authentication): Authentication {
        val jwt: String? = authentication.principal as String?

        return jwt?.let { JwtAuthenticationToken(jwtService.getAuthentication(it))  }
            ?: authentication
    }

    /**
     * 이 프로바이더가 처리 가능한 토큰인지 확인한다
     * @param authentication JwtAuthentication::class 여야 처리 가능하다
     */
    override fun supports(authentication: Class<*>): Boolean {
        return (authentication == JwtAuthenticationToken::class.java)
    }
}