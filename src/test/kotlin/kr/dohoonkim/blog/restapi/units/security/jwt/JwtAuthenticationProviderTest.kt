package kr.dohoonkim.blog.restapi.units.security.jwt

import com.nimbusds.oauth2.sdk.auth.JWTAuthentication
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthentication
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthenticationProvider
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthenticationToken
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import kr.dohoonkim.blog.restapi.support.createMember
import kr.dohoonkim.blog.restapi.support.security.createJwtAuthentication
import kr.dohoonkim.blog.restapi.support.security.createJwtConfig


internal class JwtAuthenticationProviderTest: AnnotationSpec() {

    private lateinit var authenticationProvider: JwtAuthenticationProvider
    private lateinit var jwtService: JwtService
    private lateinit var jwtAuthenticationToken: JwtAuthenticationToken
    private lateinit var member: Member

    @BeforeEach
    fun setUp() {
        member = createMember(1).first()
        jwtService = JwtService(createJwtConfig(100000, 100000))
        jwtAuthenticationToken = JwtAuthenticationToken(createJwtAuthentication(member))
        authenticationProvider = JwtAuthenticationProvider(jwtService)
    }

    @Test
    fun `JwtAuthenticationToken의 Principal에 JWT가 주어지면 JwtAuthenticatio이 반환된다`() {
        val token = JwtAuthenticationToken(jwtService.createAccessToken(member))

        authenticationProvider.supports(token::class.java) shouldBe true
        val authentication = authenticationProvider.authenticate(token)
        (authentication.principal is JwtAuthentication) shouldBe true
    }


    @AfterEach
    fun clean() {

    }
}