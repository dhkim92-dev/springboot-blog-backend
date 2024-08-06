package kr.dohoonkim.blog.restapi.units.security.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.security.handler.JwtAccessDeniedHandler
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.access.AccessDeniedException

internal class JwtAccessDeniedHandlerTest: AnnotationSpec() {

    private val objectMapper = jacksonObjectMapper()
    private val handler = JwtAccessDeniedHandler(objectMapper)

    @Test
    fun `AccessDenied 가 발생했을 경우 에러가 ErrorResponse 형태로 반환된다`() {
        val request = MockHttpServletRequest()
        val response = MockHttpServletResponse()
        val exception= mockk<AccessDeniedException>()
        every {exception.message} returns "Access denied"
        handler.handle(request, response, exception)

        response.status shouldBe 403
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}