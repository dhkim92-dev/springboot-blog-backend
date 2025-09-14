package kr.dohoonkim.blog.restapi.units.security.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.security.oauth2.handler.CustomOAuth2AuthenticationFailureHandler
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.AuthenticationException

internal class CustomOAuth2FailureHandlerTest: AnnotationSpec() {

    private val objectMapper = jacksonObjectMapper()
    private val handler = CustomOAuth2AuthenticationFailureHandler(objectMapper)

    @Test
    fun `OAuth2 인증이 실패하면 Unauthorized로 응답한다`() {
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val exception = mockk<AuthenticationException>()
        every {exception.message} returns "OAuth2 인증 실패"

        handler.onAuthenticationFailure(request,response,exception)
        response.status shouldBe 401
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}