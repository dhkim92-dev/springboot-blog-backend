package kr.dohoonkim.blog.restapi.common.response.advice

import kr.dohoonkim.blog.restapi.common.response.pagination.*
//import kr.dohoonkim.blog.restapi.common.response.pagination.CursorPaginationAspect
import kr.dohoonkim.blog.restapi.common.utility.UrlUtility
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class CursorPaginationAdvice(
    private val paginationUtil: PaginationUtil,
    private val urlUtility: UrlUtility
) : ResponseBodyAdvice<Any?> {

    override fun beforeBodyWrite(
        body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        if(body !is List<Any?>) {
            return body
        }

        val servletRequestAttributes = RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes
        val handler = servletRequestAttributes?.getAttribute("org.springframework.web.servlet.HandlerMapping.bestMatchingHandler", 0)
                as? HandlerMethod
        val cursors = handler?.methodParameters?.filter{param->param.hasParameterAnnotation(Cursor::class.java)}
            ?: return body

        return paginationUtil.toCursorList(body as List<Any>, cursors, urlUtility.getQueryParams())
    }

    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return returnType.hasMethodAnnotation(CursorPagination::class.java) //&& returnType.hasParameterAnnotation(Cursor::class.java)
    }
}