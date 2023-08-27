package kr.dohoonkim.blog.restapi.config.security

import kr.dohoonkim.blog.restapi.config.security.filter.JwtAuthenticationFilter
import kr.dohoonkim.blog.restapi.config.security.handler.*
import kr.dohoonkim.blog.restapi.config.security.jwt.*
import kr.dohoonkim.blog.restapi.config.security.provider.JwtAuthenticationProvider
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(
    private val entryPointUnauthorizedHandler: EntryPointUnauthorizedHandler,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val jwtService : JwtService,
    private val authenticationConfiguration: AuthenticationConfiguration,
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    companion object {
        private val WHITELIST_STATIC = arrayOf("/static/css/**", "/static/js/**", "*.ico", "/error")
        private val WHITELIST_SWAGGER = arrayOf("/swagger-ui", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs", "/api-docs", "/api-docs/**")
        private val AUTHENTICATION_REQUEST_ENDPOINTS = arrayOf("/api/v1/authentication", "/api/v1/reissueToken")
        private val MEMBER_QUERY_AUTHENTICATION_REQUIRED_ENDPOINTS = arrayOf("/api/v1/members/**")
        private val MEMBER_COMMAND_AUTHENTICATION_REQUIRED_ENDPOINTS = arrayOf(
            "/api/v1/comments",
            "/api/v1/comments/**",
            "/api/v1/members/**"
        )
        private val ADMIN_QUERY_AUTHENTICATION_REQUIRED_ENDPOINTS = arrayOf("/api/v1/members")
        private val ADMIN_COMMAND_AUTHENTICATION_REQUIRED_ENDPOINTS = arrayOf(
            "/api/v1/articles",
            "/api/v1/articles/**",
            "/api/v1/article-categories",
            "/api/v1/article-categories/**",
        )
        private val HTTP_COMMAND_METHODS = listOf(HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.POST, HttpMethod.DELETE)
    }

    @Bean
    fun passwordEncrypt() = BCryptPasswordEncoder(10);

    @Bean
    fun authenticationManager() : AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    fun jwtAuthenticationFilter() : JwtAuthenticationFilter {
        val matchers = MethodPathRequestMatcher()
        matchers.add(HttpMethod.GET, MEMBER_QUERY_AUTHENTICATION_REQUIRED_ENDPOINTS)
        matchers.add(HttpMethod.GET, ADMIN_QUERY_AUTHENTICATION_REQUIRED_ENDPOINTS)

        val targets = arrayOf(*MEMBER_COMMAND_AUTHENTICATION_REQUIRED_ENDPOINTS,
            *ADMIN_QUERY_AUTHENTICATION_REQUIRED_ENDPOINTS, *ADMIN_COMMAND_AUTHENTICATION_REQUIRED_ENDPOINTS)

        HTTP_COMMAND_METHODS.forEach {method ->
                matchers.add(method, targets)
        }
        matchers.build()

        return JwtAuthenticationFilter(matchers, jwtService, jwtAuthenticationProvider)
    }

    @Bean
    fun webSecurityCustomizer() :WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it.ignoring().requestMatchers(*WHITELIST_STATIC, *WHITELIST_SWAGGER)
        }
    }

    @Bean
    fun securityFilterChain(http : HttpSecurity) : SecurityFilterChain {
        http.csrf{csrf -> csrf.disable()}
            .cors{}
            .formLogin{form->form.disable()}
            .httpBasic{basic -> basic.disable()}
            .httpBasic{basic->basic.disable()}
            .sessionManagement{sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { customizer -> customizer.authenticationEntryPoint(entryPointUnauthorizedHandler) }
            .exceptionHandling { customizer -> customizer.accessDeniedHandler(jwtAccessDeniedHandler) }
            .authorizeHttpRequests { customizer->
                customizer.requestMatchers(HttpMethod.POST, "/api/v1/members").permitAll()
                customizer.requestMatchers(HttpMethod.GET, *MEMBER_QUERY_AUTHENTICATION_REQUIRED_ENDPOINTS).hasAnyRole("ADMIN", "MEMBER")
                customizer.requestMatchers(HttpMethod.DELETE, "/api/v1/members/**").hasAnyRole("ADMIN", "MEMBER")
                customizer.requestMatchers(HttpMethod.PATCH, "/api/v1/members/**").hasAnyRole("ADMIN", "MEMBER")
                HTTP_COMMAND_METHODS.forEach { method ->
                    customizer.requestMatchers(method, *MEMBER_COMMAND_AUTHENTICATION_REQUIRED_ENDPOINTS).hasAnyRole("MEMBER", "ADMIN")
                    customizer.requestMatchers(method, *ADMIN_COMMAND_AUTHENTICATION_REQUIRED_ENDPOINTS).hasRole("ADMIN")
                }
                customizer.anyRequest().permitAll()
            }

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)

        return http.build();
    }

}