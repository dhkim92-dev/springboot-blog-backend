package kr.dohoonkim.blog.restapi.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.google.common.base.CaseFormat
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.common.filter.Snake2CamelConvertFilter
import kr.dohoonkim.blog.restapi.security.resolver.MemberIdResolver
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.datetime.DateFormatter
import org.springframework.http.MediaType
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.IOException
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.ConcurrentHashMap


@Configuration
class ApplicationConfig(private val memberIdResolver: MemberIdResolver) : WebMvcConfigurer {
    
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(memberIdResolver)
    }

    @Bean
    fun jsonCustomizer(): Jackson2ObjectMapperBuilderCustomizer {
        return Jackson2ObjectMapperBuilderCustomizer { builder ->
            builder.simpleDateFormat("yyyy-MM-dd")
            builder.serializers(LocalDateSerializer(DateTimeFormatter.ISO_DATE))
            builder.serializers(LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME))
        }
    }

    @Bean
    fun snake2CamelConvertFilter() = Snake2CamelConvertFilter()

//    @Bean
//    fun snakeConverter(): OncePerRequestFilter {
//        return object : OncePerRequestFilter() {
//            @Throws(ServletException::class, IOException::class)
//            override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
//                val formattedParams: MutableMap<String, Array<String>> = ConcurrentHashMap()
//
//                for (param in request.parameterMap.keys) {
//                    val formattedParam: String = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, param)
//                    formattedParams[formattedParam] = request.getParameterValues(param)
//                }
//
//                if(request.contentType == null) {
//                    doFilter(request,response,filterChain)
//                }else if(request.contentType!!.startsWith(MediaType.MULTIPART_FORM_DATA_VALUE)) {
//                    filterChain.doFilter(object : HttpServletRequestWrapper(request) {
//                        override fun getParameter(name: String): String? {
//                            return if (formattedParams.containsKey(name)) formattedParams[name]!![0] else null
//                        }
//
//                        override fun getParameterNames(): Enumeration<String> {
//                            return Collections.enumeration(formattedParams.keys)
//                        }
//
//                        override fun getParameterValues(name: String): Array<String> {
//                            return formattedParams[name]!!
//                        }
//
//                        override fun getParameterMap(): Map<String, Array<String>> {
//                            return formattedParams
//                        }
//                    }, response)
//                }
//            }
//        }
//    }
}