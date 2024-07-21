package kr.dohoonkim.blog.restapi.common.response.advice

import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice
import java.io.InvalidClassException

@ControllerAdvice
class ApplicationCodeAdvice : ResponseBodyAdvice<Any> {

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        val applicationCode = returnType.getMethodAnnotation(ApplicationCode::class.java)
            ?: throw InvalidClassException("출력 클래스 직렬화 실패")
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