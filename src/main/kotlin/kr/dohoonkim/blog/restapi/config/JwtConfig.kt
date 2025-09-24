package kr.dohoonkim.blog.restapi.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
data class JwtConfig(
    @Value("\${jwt.issuer}") val issuer: String,
    @Value("\${jwt.audience}") val audience: String,
    @Value("\${jwt.access-token.secret}") val accessSecret: String,
    @Value("\${jwt.refresh-token.secret}") val refreshSecret: String,
    @Value("\${jwt.access-token.expiration}") val accessExpiry: Long,
    @Value("\${jwt.refresh-token.expiration}") val refreshExpiry: Long
) {
}