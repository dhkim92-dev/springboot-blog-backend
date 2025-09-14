package kr.dohoonkim.blog.restapi.support.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthentication
import kr.dohoonkim.blog.restapi.security.jwt.JwtConfig
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.Date

fun createNormalAccessToken(member: Member): String {
    val now = Date()
    val config = createJwtConfig(1000, 100000000)
    return JWT.create()
        .withIssuer(config.issuer)
        .withAudience(config.audience)
        .withExpiresAt(Date(now.time + config.accessExpiry))
        .withIssuedAt(now)
        .withSubject(member.id.toString())
        .withClaim("email", member.email)
        .withClaim("nickname", member.nickname)
        .withArrayClaim("roles", arrayOf(member.role.rolename))
        .withClaim("isActivated", member.isActivated)
        .sign(Algorithm.HMAC512(config.accessSecret))
}

fun createExpiredAccessToken(member: Member): String {
    val now = Date()
    val config = createJwtConfig(1000, 100000000)
    return JWT.create()
        .withIssuer(config.issuer)
        .withAudience(config.audience)
        .withExpiresAt(Date(now.time - 1000L))
        .withIssuedAt(Date(now.time - 10000L))
        .withSubject(member.id.toString())
        .withClaim("email", member.email)
        .withClaim("nickname", member.nickname)
        .withArrayClaim("roles", arrayOf(member.role.rolename))
        .withClaim("isActivated", member.isActivated)
        .sign(Algorithm.HMAC512(config.accessSecret))
}

fun createExpiredRefreshToken(member: Member): String {
    val now = Date()
    val config = createJwtConfig(1000, 1000)
    return JWT.create()
        .withIssuer(config.issuer)
        .withAudience(config.audience)
        .withExpiresAt(Date(now.time - 1000L))
        .withIssuedAt(Date(now.time - 10000L))
        .withSubject(member.id.toString())
        .sign(Algorithm.HMAC512(config.refreshSecret))
}

fun createJwtAuthentication(member: Member): JwtAuthentication {
    return JwtAuthentication.fromMember(member)
}

fun createJwtConfig(accessExpiry: Long, refreshExpiry: Long): JwtConfig {
    return JwtConfig(
        issuer="http://localhost:8080",
        type = "Bearer",
        audience = "http://localhost:3000",
        accessSecret = "test-access-token-secret",
        accessExpiry = accessExpiry,
        refreshSecret = "test-refresh-token-secret",
        refreshExpiry = refreshExpiry,
        header = "Authorization"
    )
}

fun createJwtService(jwtConfig: JwtConfig): JwtService {
    return JwtService(jwtConfig)
}