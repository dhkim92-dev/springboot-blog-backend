package kr.dohoonkim.blog.restapi.config.filter

import com.google.common.base.CaseFormat
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class Snake2CamelConvertFilter : OncePerRequestFilter(){

    val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val formattedParams: MutableMap<String, Array<String>> = ConcurrentHashMap()

        for (param in request.parameterMap.keys) {
            val formattedParam: String = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, param)
            formattedParams[formattedParam] = request.getParameterValues(param)
        }

        if(request.contentType.isNullOrEmpty()) {
            log.error("request content type is null!!")
            return filterChain.doFilter(request, response)
        }

        if(request.contentType!!.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)) {
            log.debug("Snake2CamelConvertFilter work here.")
            filterChain.doFilter(object : HttpServletRequestWrapper(request) {
                override fun getParameter(name: String): String? {
                    return if (formattedParams.containsKey(name)) formattedParams[name]!![0] else null
                }

                override fun getParameterNames(): Enumeration<String> {
                    return Collections.enumeration(formattedParams.keys)
                }

                override fun getParameterValues(name: String): Array<String> {
                    return formattedParams[name]!!
                }

                override fun getParameterMap(): Map<String, Array<String>> {
                    return formattedParams
                }
            }, response)
        }else {
            filterChain.doFilter(request, response)
        }
    }

}