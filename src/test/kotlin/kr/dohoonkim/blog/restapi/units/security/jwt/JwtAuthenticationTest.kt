package kr.dohoonkim.blog.restapi.units.security.jwt

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import kr.dohoonkim.blog.restapi.application.authentication.vo.JwtClaims
import kr.dohoonkim.blog.restapi.domain.member.Role
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.*

internal class JwtAuthenticationTest: AnnotationSpec() {

    private lateinit var jwtAuthentication: JwtAuthentication

    @BeforeEach
    fun setUp() {
        jwtAuthentication = JwtAuthentication(
            UUID.randomUUID(),
            "email@dohoon-kim.kr",
            "nickname",
            mutableListOf(SimpleGrantedAuthority(Role.MEMBER.rolename)),
            true
        )
    }

    @Test
    fun `getCredentails를 호출하면 null이 반환된다`() {
        jwtAuthentication.credentials shouldBe  null
    }

    @Test
    fun `getPrincipal을 호출하면 JwtClaims가 반환된다`() {
        val principal = jwtAuthentication.principal

        (principal is JwtClaims) shouldBe true
    }

    @Test
    fun `getName을 호출하면 nickname이 반환된다`() {
        jwtAuthentication.name shouldBe "nickname"
    }

    @Test
    fun `getAuthorities를 호출하면 권한 목록이 반환된다`() {
        jwtAuthentication.authorities shouldBe jwtAuthentication.roles
    }

    @Test
    fun `getDetails을 호출하면 jwtClaim이 반환된다`() {
        val detail = jwtAuthentication.details
        (detail is JwtClaims) shouldBe true
    }

    @Test
    fun `isAuthenticated를 호출하면 true가 반환된다`() {
        jwtAuthentication.isAuthenticated shouldBe true
    }

    @Test
    fun `setAuthenticated 가 호출되어도 isAuthenticated는 true여야 한다`() {
        jwtAuthentication.isAuthenticated=false
        jwtAuthentication.isAuthenticated shouldBe true
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}