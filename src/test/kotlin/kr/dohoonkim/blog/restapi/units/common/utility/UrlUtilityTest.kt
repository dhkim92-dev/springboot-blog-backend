package kr.dohoonkim.blog.restapi.units.common.utility

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockkStatic
import jakarta.servlet.http.HttpServletRequest
import kr.dohoonkim.blog.restapi.common.utility.UrlUtility
import kr.dohoonkim.blog.restapi.support.createMockHttpServletRequest
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

class UrlUtilityTest: AnnotationSpec(){

    private val urlUtility = UrlUtility()
    private lateinit var requestAttributes: RequestAttributes
    private lateinit var servletRequest: MockHttpServletRequest
    @BeforeEach
    fun setUp() {
        mockkStatic(RequestContextHolder::class)
        servletRequest = createMockHttpServletRequest()
        requestAttributes = ServletRequestAttributes(servletRequest)
        every { RequestContextHolder.currentRequestAttributes() } returns requestAttributes
    }

    @Test
    fun `http 요청이고 host가 localhost 인 경우 그대로 가져와진다`() {
        urlUtility.getURL() shouldBe "http://localhost"
    }

    @Test
    fun `http 요청이고 host가 127_0_0_1 인 경우 그대로 가져와진다`() {
        servletRequest.serverName="127.0.0.1"
        urlUtility.getURL() shouldBe "http://127.0.0.1"
    }

    @Test
    fun `http 요청이고 localhost가 아닌 경우 https로 변환된다`() {
        val servletRequest = createMockHttpServletRequest(
            serverName="www.dohoon-kim.kr",
            serverPort = 443,
            scheme = "https"
        )
        val expectedHost = "https://www.dohoon-kim.kr"
        every { RequestContextHolder.currentRequestAttributes() } returns ServletRequestAttributes(servletRequest)
        val url = urlUtility.getURL()
        url shouldBe expectedHost
    }

    @Test
    fun `https로 시작하는 경우 그대로 반환된다`() {
        servletRequest.scheme = "https"
        servletRequest.serverPort = 443
        every { RequestContextHolder.currentRequestAttributes() } returns ServletRequestAttributes(servletRequest)
        val url = urlUtility.getURL()
        url shouldBe "https://localhost"
    }

    @Test
    fun `QueryString이 Map으로 반환된다`() {
        servletRequest.setParameter("category", "1")
//        every { RequestContextHolder.currentRequestAttributes() } returns ServletRequestAttributes(MockHttpServletRequest())
        val queries = urlUtility.getQueryParams()
        queries["category"] shouldBe "1"
    }

    @Test
    fun `RequestContextHolder에 리퀘스트 정보가 존재하지 않으면 빈 Map이 반환된다`() {
        val servletRequest = MockHttpServletRequest()
        every { RequestContextHolder.currentRequestAttributes() } returns ServletRequestAttributes(MockHttpServletRequest())
        val queries = urlUtility.getQueryParams()
        queries.isEmpty() shouldBe true
    }

    @AfterEach()
    fun clean() {
        clearAllMocks()
    }
}