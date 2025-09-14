package kr.dohoonkim.blog.restapi.config

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import kr.dohoonkim.blog.restapi.common.filter.MultiPartSnake2CamelFilter
import kr.dohoonkim.blog.restapi.security.resolver.MemberIdResolver
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.format.DateTimeFormatter


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
    fun multipartSnake2CamelFilter() = MultiPartSnake2CamelFilter()

    @Bean
    fun webClient() = WebClient.builder().build()

    @Bean
    fun passwordEncrypt(): PasswordEncoder {
        return BCryptPasswordEncoder(10)
    }
}