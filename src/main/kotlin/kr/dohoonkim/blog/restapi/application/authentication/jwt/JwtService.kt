package kr.dohoonkim.blog.restapi.application.authentication.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import kr.dohoonkim.blog.restapi.config.JwtConfig
import kr.dohoonkim.blog.restapi.domain.member.Member
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class JwtService(
    private val config: JwtConfig
) {

    private val accessTokenAlgorithm = Algorithm.HMAC256(config.accessSecret)

    private val refreshTokenAlgorithm = Algorithm.HMAC256(config.refreshSecret)

    private val accessTokenVerifier = JWT.require(accessTokenAlgorithm)
        .withIssuer(config.issuer)
        .withAudience(config.audience)
        .build()

    private val refreshTokenVerifier = JWT.require(refreshTokenAlgorithm)
        .withIssuer(config.issuer)
        .withAudience(config.audience)
        .build()

    fun generateAccessToken(member: Member): String {
        val now = Instant.now()
        val expiresAt = now.plus(config.accessExpiry, ChronoUnit.MILLIS)

        return JWT.create()
            .withIssuer(config.issuer)
            .withSubject(member.id!!.toString())
            .withAudience(config.audience)
            .withIssuedAt(now)
            .withExpiresAt(expiresAt)
            .withClaim("email", member.email)
            .withClaim("nickname", member.nickname)
            .withArrayClaim("roles", arrayOf(member.role.rolename))
            .withClaim("isActivated", !member.isBlocked)
            .sign(accessTokenAlgorithm)
    }


    fun generateRefreshToken(member: Member): String {
        val now = Instant.now()
        val expiresAt = now.plus(config.refreshExpiry, ChronoUnit.MILLIS)

        return JWT.create()
            .withIssuer(config.issuer)
            .withSubject(member.id!!.toString())
            .withAudience(config.audience)
            .withIssuedAt(now)
            .withExpiresAt(expiresAt)
            .sign(refreshTokenAlgorithm)
    }

    fun validateAccessToken(token: String): DecodedJWT {
        return accessTokenVerifier.verify(token)
    }

    fun validateRefreshToken(token: String): DecodedJWT {
        return refreshTokenVerifier.verify(token)
    }

    fun decodeRefreshToken(token: String): DecodedJWT {
        return JWT.decode(token)
    }
}