package kr.dohoonkim.blog.restapi.units.common.filter

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import kr.dohoonkim.blog.restapi.common.filter.MultiPartSnake2CamelFilter
import org.springframework.mock.web.MockFilterChain
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse


internal class Snake2CamelConvertFilterTest: AnnotationSpec() {

    private lateinit var filter: MultiPartSnake2CamelFilter
    private lateinit var request: MockHttpServletRequest
    private lateinit var response: MockHttpServletResponse
    private lateinit var filterChain: MockFilterChain

    @BeforeEach
    fun setUp() {
        filter = MultiPartSnake2CamelFilter()
        request = MockHttpServletRequest()
        response = MockHttpServletResponse()
    }

    @Test
    fun `MultiParFormData이 주어져도 key가 Camel로 변환된다`() {
        request.contentType = "multipart/form-data"
        request.setParameter("first_name", "a")
        request.setParameter("last_name", "b")

        filterChain = object : MockFilterChain() {
            override fun doFilter(request: ServletRequest, response: ServletResponse) {
                // Validate inside MockFilterChain
                val mockRequest = request as HttpServletRequest
                val camelCaseParams = mockRequest.parameterMap

                camelCaseParams["firstName"] shouldBe arrayOf("a")
                camelCaseParams["lastName"] shouldBe arrayOf("b")

                super.doFilter(request, response)
            }
        }

        filter.doFilter(request, response, filterChain)
    }
}