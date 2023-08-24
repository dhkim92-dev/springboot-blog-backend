package kr.dohoonkim.blog.restapi.units.utility

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.config.security.jwt.JwtConfig
import kr.dohoonkim.blog.restapi.config.security.jwt.JwtService
import kr.dohoonkim.blog.restapi.application.authentication.dto.JwtClaims
import kr.dohoonkim.blog.restapi.domain.member.Role
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*

class JwtServiceTest : AnnotationSpec() {

    companion object {
        val jwtConfig: JwtConfig = JwtConfig(
            "test-issuer",
            "Bearer",
            "Authorization",
            "test-audience",
            "access-secret",
            "refresh-secret",
            86400000,
            86400000
        )
    }
    private val jwtService : JwtService = JwtService(jwtConfig)
    private val memberId = UUID.randomUUID()

    @Test
    fun 액세스토큰_검증() {
        val claims = JwtClaims(
                id = memberId,
                email = "test01@gmail.com",
                nickname = "test01",
                roles = arrayOf(Role.ADMIN.rolename),
                isActivated = true
        )

        val token = jwtService.createAccessToken(jwtClaim = claims)
        val decodedJwt = jwtService.verifyAccessToken(token)

        decodedJwt.subject shouldBe memberId.toString()
        decodedJwt.getClaim("email").asString() shouldBe "test01@gmail.com"
        decodedJwt.getClaim("nickname").asString() shouldBe "test01"
        decodedJwt.getClaim("roles").asArray(String::class.java) shouldBe arrayOf(Role.ADMIN.rolename)
    }

    @Test
    fun 액세스토큰_2_JwtAuthentication() {
        val claims = JwtClaims(
                id = memberId,
                email = "test01@gmail.com",
                nickname = "test01",
                roles = arrayOf(Role.ADMIN.rolename),
                isActivated = true
        )
        val token = jwtService.createAccessToken(jwtClaim = claims)
        val authentication = jwtService.getAuthentication(token)

        authentication.authorities shouldBe mutableListOf(SimpleGrantedAuthority(Role.ADMIN.rolename))
    }
}