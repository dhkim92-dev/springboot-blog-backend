package kr.dohoonkim.blog.restapi.units.security.jwt

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
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
    fun `password가 "" 이다`() {
        jwtAuthentication.password shouldBe ""
    }

    @Test
    fun `nickname을 호출하면 nickname이 반환된다`() {
        jwtAuthentication.nickname shouldBe "nickname"
    }

    @Test
    fun `권한 목록을 조회하면 JwtAuthentication의 roles와 일치한다`() {
        jwtAuthentication.authorities shouldBe jwtAuthentication.roles
    }

    fun clean() {
        clearAllMocks()
    }
}