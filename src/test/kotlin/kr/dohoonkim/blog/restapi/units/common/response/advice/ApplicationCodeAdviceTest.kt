package kr.dohoonkim.blog.restapi.units.common.response.advice

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.advice.ApplicationCodeAdvice
import kr.dohoonkim.blog.restapi.support.mockApplicationCodeMethodParameter
import kr.dohoonkim.blog.restapi.support.mockServletHttpRequest
import kr.dohoonkim.blog.restapi.support.mockServletHttpResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.http.server.ServletServerHttpResponse


internal class ApplicationCodeAdviceTest: AnnotationSpec() {

    private val applicationCodeAdvice = ApplicationCodeAdvice()
    private val convertType = StringHttpMessageConverter::class.java

    @Test
    fun `ApplicationCode가 있으면 ApiResult로 반환된다`() {
        val data = 1000L as Any?
        val parameter = mockApplicationCodeMethodParameter(code = ResultCode.HEALTH_CHECK_SUCCESS)
        val request = mockServletHttpRequest()
        val response = mockServletHttpResponse(HttpStatus.OK)
        val result = applicationCodeAdvice.beforeBodyWrite(data,
            parameter,
            MediaType.APPLICATION_JSON,
            convertType,
            request,
            response
        )

        (result is ApiResult<*>) shouldBe true
        val wrap = result as ApiResult<Long>

        wrap.code shouldBe ResultCode.HEALTH_CHECK_SUCCESS.code
        wrap.message shouldBe ResultCode.HEALTH_CHECK_SUCCESS.message
        wrap.data shouldBe data
        wrap.status shouldBe ResultCode.HEALTH_CHECK_SUCCESS.status.value()
    }

    @Test
    fun `ApplicationCode가 포함되어 있으면 처리 가능하다`() {
        val parameter = mockApplicationCodeMethodParameter(code = ResultCode.HEALTH_CHECK_SUCCESS)
        applicationCodeAdvice.supports(parameter,convertType) shouldBe true
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }

}