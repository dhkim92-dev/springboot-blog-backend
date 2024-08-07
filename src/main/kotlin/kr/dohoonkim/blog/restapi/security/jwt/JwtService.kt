package kr.dohoonkim.blog.restapi.security.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.AlgorithmMismatchException
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.Claim
import com.auth0.jwt.interfaces.DecodedJWT
import jakarta.servlet.http.HttpServletRequest
import kr.dohoonkim.blog.restapi.common.error.exceptions.ExpiredTokenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.JwtInvalidException
import kr.dohoonkim.blog.restapi.domain.member.Member
import org.slf4j.LoggerFactory
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(private val config: JwtConfig) {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val accessTokenVerifier = JWT.require(Algorithm.HMAC512(config.accessSecret))
        .withIssuer(config.issuer)
        .build()

    private val refreshTokenVerifier = JWT.require(Algorithm.HMAC512(config.refreshSecret))
        .withIssuer(config.issuer)
        .build()

    private val ACCESS_TOKEN_CLAIM_KEYS = listOf("email", "nickname", "roles", "isActivated")

    fun createAccessToken(member: Member): String {
        val now = Date()

        return JWT.create()
            .withIssuer(config.issuer)
            .withSubject(member.id.toString())
            .withAudience(config.audience)
            .withIssuedAt(now)
            .withExpiresAt(Date(now.time + config.accessExpiry))
            .withClaim("email", member.email)
            .withClaim("nickname", member.nickname)
            .withArrayClaim("roles", arrayOf(member.role.rolename))
            .withClaim("isActivated", member.isActivated)
            .sign(Algorithm.HMAC512(config.accessSecret))
    }

    fun createRefreshToken(member: Member): String {
        val now = Date()
        return JWT.create()
            .withIssuer(config.issuer)
            .withSubject(member.id.toString())
            .withAudience(config.audience)
            .withIssuedAt(now)
            .withExpiresAt(Date(now.time + config.refreshExpiry))
            .sign(Algorithm.HMAC512(config.refreshSecret))
    }

    fun verifyAccessToken(token: String): DecodedJWT {
        return accessTokenVerifier.verify(token);
    }

    fun verifyRefreshToken(token: String): DecodedJWT {
        return refreshTokenVerifier.verify(token)
    }



    /**
     * JWT를 받아 JwtAuthenticationToken 을 생성한다.
     * 이 메소드는 Authorization Header에 Bearer 토큰이 포함되어 있을 경우에만 실행된다.
     * @param token : String, JWT Access Token
     * @return [JwtAuthenticationToken]]
     * @throws JwtInvalidException JWT Token의 클레임에 필수 필드가 존재하지 않음
     * @throws TokenExpiredException 유효기간이 만료된 토큰
     * @throws SignatureVerificationException 변조된 JWT
     * @throws AlgorithmMismatchException 알고리즘이 일치하지 않음
     */
    fun getAuthentication(token: String): JwtAuthentication {
        val decodedJWT = JWT.decode(token)
        if(!checkContainsKeys(decodedJWT)) {
            throw JwtInvalidException()
        }

        val jwt: DecodedJWT = this.verifyAccessToken(token)
        return JwtAuthentication.fromDecodedJwt(jwt)
    }

    private fun checkContainsKeys(jwt: DecodedJWT): Boolean {
        return ACCESS_TOKEN_CLAIM_KEYS.map { jwt.claims.containsKey(it) }
            .reduce{ a, b -> a&&b }
    }
}