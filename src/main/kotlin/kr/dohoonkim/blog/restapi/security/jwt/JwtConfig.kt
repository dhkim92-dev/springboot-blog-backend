package kr.dohoonkim.blog.restapi.security.jwt

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.util.*

@Configuration
data class JwtConfig(
    @Value("\${jwt.issuer}") val issuer: String,
    @Value("\${jwt.type}") val type: String,
    @Value("\${jwt.header}") val header: String,
    @Value("\${jwt.audience}") val audience: String,
    @Value("\${jwt.access-token.secret}") val accessSecret: String,
    @Value("\${jwt.refresh-token.secret}") val refreshSecret: String,
    @Value("\${jwt.access-token.expiry}") val accessExpiry: Long,
    @Value("\${jwt.refresh-token.expiry}") val refreshExpiry: Long
) {
}
