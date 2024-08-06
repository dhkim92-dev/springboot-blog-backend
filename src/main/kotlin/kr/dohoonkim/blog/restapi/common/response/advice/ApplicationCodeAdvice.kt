package kr.dohoonkim.blog.restapi.common.response.advice

import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

/**
 * Response Body를 ApiResult로 Wrapping 을 수행하는 클래스
 * ApplicationCode 어노테이션이 적용된 엔드포인트에 대해서만 이 어드바이스가 동작해야한다
 */
@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
class ApplicationCodeAdvice() : ResponseBodyAdvice<Any> {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        val applicationCode = returnType.getMethodAnnotation(ApplicationCode::class.java)!!
        val resultCode = applicationCode.code
        response.setStatusCode(resultCode.status)


        return ApiResult(
            status = resultCode.status.value(),
            code = resultCode.code,
            data = body,
            message = resultCode.message
        )
    }

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return returnType.hasMethodAnnotation(ApplicationCode::class.java)
    }
}