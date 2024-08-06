package kr.dohoonkim.blog.restapi.support.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import kr.dohoonkim.blog.restapi.application.authentication.vo.JwtClaims
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthentication
import kr.dohoonkim.blog.restapi.security.jwt.JwtConfig
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.Date

fun createNormalAccessToken(jwtClaims: JwtClaims): String {
    val now = Date()
    val config = createJwtConfig(1000, 100000000)
    return JWT.create()
        .withIssuer(config.issuer)
        .withAudience(config.audience)
        .withExpiresAt(Date(now.time + config.accessExpiry))
        .withIssuedAt(now)
        .withSubject(jwtClaims.id.toString())
        .withClaim("email", jwtClaims.email)
        .withClaim("nickname", jwtClaims.nickname)
        .withArrayClaim("roles", jwtClaims.roles)
        .withClaim("isActivated", jwtClaims.isActivated)
        .sign(Algorithm.HMAC512(config.accessSecret))
}

fun createExpiredAccessToken(jwtClaims: JwtClaims): String {
    val now = Date()
    val config = createJwtConfig(1000, 100000000)
    return JWT.create()
        .withIssuer(config.issuer)
        .withAudience(config.audience)
        .withExpiresAt(Date(now.time - 1000L))
        .withIssuedAt(Date(now.time - 10000L))
        .withSubject(jwtClaims.id.toString())
        .withClaim("email", jwtClaims.email)
        .withClaim("nickname", jwtClaims.nickname)
        .withArrayClaim("roles", jwtClaims.roles)
        .withClaim("isActivated", jwtClaims.isActivated)
        .sign(Algorithm.HMAC512(config.accessSecret))
}

fun createExpiredRefreshToken(jwtClaims: JwtClaims): String {
    val now = Date()
    val config = createJwtConfig(1000, 1000)
    return JWT.create()
        .withIssuer(config.issuer)
        .withAudience(config.audience)
        .withExpiresAt(Date(now.time - 1000L))
        .withIssuedAt(Date(now.time - 10000L))
        .withSubject(jwtClaims.id.toString())
        .sign(Algorithm.HMAC512(config.refreshSecret))
}

fun createJwtAuthentication(member: Member): JwtAuthentication {
    return JwtAuthentication(
        member.id,
        member.email,
        member.nickname,
        mutableListOf(SimpleGrantedAuthority(member.role.rolename)),
        member.isActivated
    )
}

fun createJwtClaims(member: Member): JwtClaims {
    return JwtClaims(
        id = member.id,
        email = member.email,
        nickname = member.nickname,
        isActivated = member.isActivated,
        roles = arrayOf(member.role.rolename)
    )
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