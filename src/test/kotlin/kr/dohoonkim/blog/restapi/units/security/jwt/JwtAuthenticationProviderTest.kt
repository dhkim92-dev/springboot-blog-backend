package kr.dohoonkim.blog.restapi.units.security.jwt

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.authentication.vo.JwtClaims
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthentication
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthenticationProvider
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthenticationToken
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import kr.dohoonkim.blog.restapi.support.createMember
import kr.dohoonkim.blog.restapi.support.security.createJwtClaims
import kr.dohoonkim.blog.restapi.support.security.createJwtConfig


internal class JwtAuthenticationProviderTest: AnnotationSpec() {

    private lateinit var authenticationProvider: JwtAuthenticationProvider
    private lateinit var jwtService: JwtService
    private lateinit var jwtClaims: JwtClaims

    @BeforeEach
    fun setUp() {
        jwtService = JwtService(createJwtConfig(100000, 100000))
        jwtClaims = createJwtClaims(createMember(1).get(0))
        authenticationProvider = JwtAuthenticationProvider(jwtService)
    }

    @Test
    fun `JwtAuthenticationToken이 주어지면 JwtAuthentication이 반환된다`() {
        val token = JwtAuthenticationToken(jwtService.createAccessToken(jwtClaims))

        authenticationProvider.supports(token::class.java) shouldBe true
        val authentication = authenticationProvider.authenticate(token)
        (authentication is JwtAuthentication) shouldBe true
    }


    @AfterEach
    fun clean() {

    }
}