package kr.dohoonkim.blog.restapi.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.servlet.http.HttpServletRequest
import kr.dohoonkim.blog.restapi.common.error.exceptions.ExpiredTokenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.JwtInvalidException
import kr.dohoonkim.blog.restapi.security.authentication.JwtAuthentication
import kr.dohoonkim.blog.restapi.application.authentication.dto.JwtClaims
import org.slf4j.LoggerFactory
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(private val config: JwtConfig) {

    private val log = LoggerFactory.getLogger(JwtService::class.java)

    private val accessTokenVerifier = JWT.require(Algorithm.HMAC512(config.accessSecret))
        .withIssuer(config.issuer)
        .build()

    private val refreshTokenVerifier = JWT.require(Algorithm.HMAC512(config.refreshSecret))
        .withIssuer(config.issuer)
        .build()

    fun createAccessToken(jwtClaim: JwtClaims): String {
        val now = Date()

        return JWT.create()
            .withIssuer(config.issuer)
            .withSubject(jwtClaim.id.toString())
            .withAudience(config.audience)
            .withIssuedAt(now)
            .withExpiresAt(Date(now.time + config.accessExpiry))
            .withClaim("email", jwtClaim.email)
            .withClaim("nickname", jwtClaim.nickname)
            .withArrayClaim("roles", jwtClaim.roles)
            .withClaim("isActivated", jwtClaim.isActivated)
            .sign(Algorithm.HMAC512(config.accessSecret))
    }

    fun createRefreshToken(jwtClaim: JwtClaims): String {
        val now = Date()
        return JWT.create()
            .withIssuer(config.issuer)
            .withSubject(jwtClaim.id.toString())
            .withAudience(config.audience)
            .withIssuedAt(now)
            .withExpiresAt(Date(now.time + config.refreshExpiry))
            .sign(Algorithm.HMAC512(config.refreshSecret))
    }

    fun verifyAccessToken(token: String): DecodedJWT {
        return this.accessTokenVerifier.verify(token);
    }

    fun verifyRefreshToken(token: String): DecodedJWT {
        return refreshTokenVerifier.verify(token)
    }

    fun extractBearerTokenFromHeader(request: HttpServletRequest): String? {
        var token: String? = request.getHeader(this.config.header)

        if (token == null) {
            return null
        }

        if (!token.startsWith(config.type, true)) {
            return null
        }

        return token.substring(7);
    }

    /**
     * JWT Access Token으로 부터 JwtAuthentication 을 생성한다.
     * @param token : String, JWT Access Token
     * @return [JwtAuthentication]
     */

    fun getAuthentication(token: String): JwtAuthentication {
        try {
            val jwt: DecodedJWT = this.verifyAccessToken(token)
            val memberId: UUID = UUID.fromString(jwt.subject)
            val email: String = jwt.getClaim("email").asString()
            val nickname: String = jwt.getClaim("nickname").asString()
            val roles: MutableCollection<out GrantedAuthority> = jwt.getClaim("roles").asArray(String::class.java)
                .map { rolename -> SimpleGrantedAuthority(rolename) }
                .toMutableList()
            val isActivated = jwt.getClaim("isActivated").asBoolean()
            return JwtAuthentication(memberId, email, nickname, roles, isActivated)
        } catch (e: TokenExpiredException) {
            throw ExpiredTokenException()
        } catch (e: Exception) {
            throw JwtInvalidException()
        }
    }
}