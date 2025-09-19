package kr.dohoonkim.blog.restapi.common.security.jwt

import kr.dohoonkim.blog.restapi.application.authentication.jwt.JwtService
import kr.dohoonkim.blog.restapi.common.security.LoginInfo
import kr.dohoonkim.blog.restapi.domain.member.Role
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.UUID

class JwtAuthenticationProvider(
    private val jwtService: JwtService
): AuthenticationProvider {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun supports(authentication: Class<*>?): Boolean {
        return JwtAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    override fun authenticate(authentication: Authentication?): Authentication? {
        authentication as JwtAuthenticationToken

        val jwt = authentication.credentials as String

        val decodedJWT = try {
            jwtService.validateAccessToken(token = jwt)
        } catch (e: Exception) {
            throw BadCredentialsException("JWTVerificationException", e)
        }

        val roles = decodedJWT.getClaim("roles").asList(String::class.java)
            .map {
                when (it) {
                    "ROLE_MEMBER" -> Role.MEMBER
                    "ROLE_ADMIN" -> Role.ADMIN
                    else -> throw BadCredentialsException("INVALID_ROLE")
                }
            } ?: emptyList<Role>()

        val authorities = roles.map { it -> SimpleGrantedAuthority(it.rolename) }
        val loginInfo = LoginInfo(
            memberId = UUID.fromString(decodedJWT.subject),
            nickname = decodedJWT.getClaim("nickname").asString(),
            roles = roles
        )

        return JwtAuthenticationToken(
            principal = loginInfo,
            credentials = null,
            authorities = authorities
        )
    }
}