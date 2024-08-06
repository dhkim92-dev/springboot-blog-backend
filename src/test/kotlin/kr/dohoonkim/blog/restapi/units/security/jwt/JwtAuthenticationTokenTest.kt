package kr.dohoonkim.blog.restapi.units.security.jwt

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthenticationToken

internal class JwtAuthenticationTokenTest: AnnotationSpec() {

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun `getCredential을 호출하면 항상 nul이어야 한다`() {
        val token = JwtAuthenticationToken(null)

        token.credentials shouldBe null
    }

    @Test
    fun `인증되지 않은 상태에서 getPrincipal을 호출하면 null이 반환된다`() {
        val token = JwtAuthenticationToken(null)

        token.principal shouldBe null
    }

    @Test
    fun `인증 토큰 문자열이 입력된 상태에서 getPrincipal을 호출하면 해당 token이 반환된다`() {
        val token = JwtAuthenticationToken("token")
        token.principal shouldNotBe null
        token.principal shouldBe "token"
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}