package kr.dohoonkim.blog.restapi.security.provider

import kr.dohoonkim.blog.restapi.config.security.jwt.JwtAuthenticationToken
import kr.dohoonkim.blog.restapi.security.jwt.JwtConfig
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class JwtAuthenticationProvider(
    private val jwtConfig : JwtConfig,
    private val jwtService : JwtService
) : AuthenticationProvider {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * @param authentication : [JwtAuthentication]
     */
    override fun authenticate(authentication: Authentication): Authentication {
        val jwt : String = authentication.principal as String // authentication
        return jwtService.getAuthentication(jwt)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == JwtAuthenticationToken::class
    }
}