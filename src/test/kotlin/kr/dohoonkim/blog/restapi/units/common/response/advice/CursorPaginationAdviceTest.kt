package kr.dohoonkim.blog.restapi.units.common.response.advice

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.common.response.CursorList
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.advice.CursorPaginationAdvice
import kr.dohoonkim.blog.restapi.common.response.pagination.CursorPagination
import kr.dohoonkim.blog.restapi.common.response.pagination.PaginationUtil
import kr.dohoonkim.blog.restapi.common.utility.UrlUtility
import kr.dohoonkim.blog.restapi.support.mockApplicationCodeMethodParameter
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.method.HandlerMethod
internal class CursorPaginationAdviceTest: AnnotationSpec() {

    private val paginationUtil = mockk<PaginationUtil>()
    private val urlUtility = mockk<UrlUtility>()
    private lateinit var cursorPaginationAdvice: CursorPaginationAdvice
    private lateinit var requestAttributes: ServletRequestAttributes
    private lateinit var handlerMethod: HandlerMethod
    private lateinit var methodParameter: MethodParameter
    private lateinit var cursorPagination: CursorPagination
    private lateinit var request: ServerHttpRequest
    private lateinit var response: ServerHttpResponse

    @BeforeEach
    fun setUp() {
        cursorPaginationAdvice = CursorPaginationAdvice(paginationUtil, urlUtility)
        requestAttributes = mockk<ServletRequestAttributes>(relaxed = true)
        handlerMethod = mockk<HandlerMethod>(relaxed = true)
        methodParameter = mockk<MethodParameter>(relaxed = true)
        cursorPagination = mockk<CursorPagination>(relaxed = true)
        request = mockk<ServerHttpRequest>(relaxed = true)
        response = mockk<ServerHttpResponse>(relaxed = true)

        RequestContextHolder.setRequestAttributes(requestAttributes)

        every { requestAttributes.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler", 0) } returns handlerMethod
        every { handlerMethod.methodParameters } returns arrayOf(methodParameter)
        every { methodParameter.hasParameterAnnotation(CursorPagination::class.java) } returns true
        every { methodParameter.getParameterAnnotation(CursorPagination::class.java) } returns cursorPagination
        every { paginationUtil.toCursorList(any(), any(), any()) } returns CursorList.of(listOf(3), null, 3)
        every { urlUtility.getQueryParams() } returns mapOf()
    }

    @Test
    fun `CursorPagination이 포함된 메소드가 전달되면 CursorList가 반환된다`() {
        val body = listOf(0, 1, 2, 3)
        val result = executeBodyRequest(body)

        shouldNotThrowAny {
            val cursorList = result as CursorList<Any?>
        }
    }

    @Test
    fun `CursorPagination이 포함된 메서드에 전달된 데이터가 List가 아니면 원래 body가 반환된다`() {
        val body = 1L
        val result = executeBodyRequest(body)

        (result !is CursorList<*>) shouldBe true
        result shouldBe body
    }

    @Test
    fun `CursorPagination이 포함되지 않은 method의 supports 결과는 false`() {
        val param = mockk<MethodParameter>()
        every { param.hasMethodAnnotation(CursorPagination::class.java) } returns false
        cursorPaginationAdvice.supports(param, StringHttpMessageConverter::class.java) shouldBe false
    }

    @Test
    fun `CursorPagination이 포함된 method의 support 결과는 true`() {
        val param = mockk<MethodParameter>()
        every { param.hasMethodAnnotation(CursorPagination::class.java) } returns true
        cursorPaginationAdvice.supports(param, StringHttpMessageConverter::class.java) shouldBe true
    }

    @AfterEach
    fun clean() {
        RequestContextHolder.resetRequestAttributes()
        clearAllMocks()
    }

    private fun executeBodyRequest(body: Any?): Any? {
        return cursorPaginationAdvice.beforeBodyWrite(
            body,
            methodParameter,
            MediaType.APPLICATION_JSON,
            StringHttpMessageConverter::class.java,
            request,
            response
        )
    }
}