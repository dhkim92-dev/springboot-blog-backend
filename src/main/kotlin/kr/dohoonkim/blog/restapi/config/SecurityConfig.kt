package kr.dohoonkim.blog.restapi.config

import com.fasterxml.jackson.databind.ObjectMapper
import kr.dohoonkim.blog.restapi.application.authentication.jwt.JwtService
import kr.dohoonkim.blog.restapi.common.security.exception.JwtAccessDeniedHandler
import kr.dohoonkim.blog.restapi.common.security.exception.JwtEntrypointUnauthorizedHandler
import kr.dohoonkim.blog.restapi.common.security.jwt.JwtAuthenticationFilter
import kr.dohoonkim.blog.restapi.common.security.jwt.JwtAuthenticationProvider
import kr.dohoonkim.blog.restapi.domain.member.Role
//import kr.dohoonkim.blog.restapi.security.oauth2.CustomOAuth2UserService
//import kr.dohoonkim.blog.restapi.security.handler.*
//import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthenticationFilter
//import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthenticationProvider
//import kr.dohoonkim.blog.restapi.security.jwt.JwtService
//import kr.dohoonkim.blog.restapi.security.oauth2.HttpCookieOAuth2AuthorizationRepository
//import kr.dohoonkim.blog.restapi.security.oauth2.handler.CustomOAuth2AuthenticationFailureHandler
//import kr.dohoonkim.blog.restapi.security.oauth2.handler.CustomOAuth2AuthenticationSuccessHandler
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(
//    private val authenticationConfiguration: AuthenticationConfiguration,
//    private val oAuth2AuthorizationRepository: HttpCookieOAuth2AuthorizationRepository,
//    private val oAuth2AuthenticationSuccessHandler: CustomOAuth2AuthenticationSuccessHandler,
//    private val oAuth2AuthenticationFailureHandler: CustomOAuth2AuthenticationFailureHandler,
//    private val customOAuth2UserService: CustomOAuth2UserService,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Bean
    fun entrypointUnauthorizedHandler(om: ObjectMapper): JwtEntrypointUnauthorizedHandler {
        return JwtEntrypointUnauthorizedHandler(om)
    }

    @Bean
    fun accessDeniedHandler(om: ObjectMapper): JwtAccessDeniedHandler {
        return JwtAccessDeniedHandler(om)
    }

    @Bean
    fun jwtAuthenticationProvider(jwtService: JwtService): JwtAuthenticationProvider {
        return JwtAuthenticationProvider(jwtService)
    }

    @Bean
    fun authenticationManager(jwtAuthenticationProvider: JwtAuthenticationProvider): AuthenticationManager {
        return ProviderManager(jwtAuthenticationProvider)
    }

//    @Bean
//    fun webSecurityCustomizer(): WebSecurityCustomizer {
//        return WebSecurityCustomizer {
//            it.ignoring().requestMatchers(*WHITELIST_STATIC, *WHITELIST_SWAGGER)
//        }
//    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        authenticationManager: AuthenticationManager,
        jwtEntryPointUnauthorizedHandler: JwtEntrypointUnauthorizedHandler,
        jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    ): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
            .cors {}
            .formLogin { form -> form.disable() }
            .httpBasic { basic -> basic.disable() }
            .sessionManagement {
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationManager(authenticationManager)
            .addFilterBefore(JwtAuthenticationFilter(authenticationManager), UsernamePasswordAuthenticationFilter::class.java )
            .exceptionHandling { customizer ->
                customizer.accessDeniedHandler(jwtAccessDeniedHandler)
                customizer.authenticationEntryPoint(jwtEntryPointUnauthorizedHandler)
            }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui/index.html",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/actuator/**",
                    "/prometheus/**",
                    "/health-check",
                    "/api/v1/auth/jwt/reissue"
                ).permitAll()
                it.requestMatchers(HttpMethod.POST, "/api/v1/auth/email-password").permitAll()
                it.requestMatchers("/api/v1/members/**").hasAnyRole("MEMBER", "ADMIN")
                it.requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
                it.requestMatchers("/api/v1/files/**").hasAnyRole("ADMIN")
                it.anyRequest().authenticated()
            }
//            .oauth2Login { customizer ->
//                customizer.authorizationEndpoint { config->
////                    authorization.baseUri("/api/v1/authentication/oauth2")
//                    config.baseUri("/api/v1/authentication/oauth2")
//                    config.authorizationRequestRepository(oAuth2AuthorizationRepository)
//                }
//                customizer.redirectionEndpoint { redirectEndpoint ->
//                    redirectEndpoint.baseUri("/api/v1/authentication/callback/*")
//                }
//                customizer.userInfoEndpoint {
//                    it.userService(customOAuth2UserService)
//                }
//                customizer.successHandler(oAuth2AuthenticationSuccessHandler)
//                customizer.failureHandler(oAuth2AuthenticationFailureHandler)
//            }
//        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
//        http.addFilterBefore(jwtExceptionFilter(), JwtAuthenticationFilter      ::class.java)
        return http.build();
    }

}