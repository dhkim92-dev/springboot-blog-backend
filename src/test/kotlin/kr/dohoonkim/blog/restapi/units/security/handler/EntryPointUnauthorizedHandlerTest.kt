package kr.dohoonkim.blog.restapi.units.security.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.security.handler.EntryPointUnauthorizedHandler
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.AuthenticationException

internal class EntryPointUnauthorizedHandlerTest: AnnotationSpec() {

    private val handler = EntryPointUnauthorizedHandler(jacksonObjectMapper() )

    @Test
    fun `필터 처리 과정중 예외가 발생했을 경우 에러를 ErrorResponse로 반환한다`() {
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val exception = mockk <AuthenticationException>()
        every {exception.message} returns "jwt authentication exception"
        handler.commence(request, response, exception)

        response.status shouldBe 401
        response.contentType shouldBe MediaType.APPLICATION_JSON_UTF8_VALUE
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}