package kr.dohoonkim.blog.restapi.common.filter

import com.google.common.base.CaseFormat

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

/**
 * MediaType이 Multipart/FormData 인 경우 Snake2Camel 표기 변환을 수행하는 필터
 */
class MultiPartSnake2CamelFilter : OncePerRequestFilter() {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.contentType?.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE) == true) {
            val formattedParams = formatParameters(request.parameterMap)
            val wrappedRequest = createWrappedRequest(request, formattedParams)
            filterChain.doFilter(wrappedRequest, response)
        } else {
            filterChain.doFilter(request, response)
        }
    }

    private fun formatParameters(parameterMap: Map<String, Array<String>>): Map<String, Array<String>> {
        return parameterMap.mapKeys {
            CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, it.key)
        }.toMap()
    }

    private fun createWrappedRequest(
        originalRequest: HttpServletRequest,
        formattedParams: Map<String, Array<String>>
    ): HttpServletRequestWrapper {
        return object : HttpServletRequestWrapper(originalRequest) {
            override fun getParameter(name: String): String? {
                return formattedParams[name]?.firstOrNull()
            }

            override fun getParameterNames(): Enumeration<String> {
                return Collections.enumeration(formattedParams.keys)
            }

            override fun getParameterValues(name: String): Array<String> {
                return formattedParams[name] ?: arrayOf()
            }

            override fun getParameterMap(): Map<String, Array<String>> {
                return formattedParams
            }
        }
    }
}