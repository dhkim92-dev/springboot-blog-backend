package kr.dohoonkim.blog.restapi.security.jwt

import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(
    private val jwtConfig: JwtConfig,
    private val jwtService: JwtService
) : AuthenticationProvider {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun authenticate(authentication: Authentication): Authentication {
        val jwt: String = authentication.principal as String // authentication
        return jwtService.getAuthentication(jwt)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == JwtAuthenticationToken::class
    }
}