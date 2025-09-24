package kr.dohoonkim.blog.restapi.config

import com.fasterxml.jackson.databind.ObjectMapper
import kr.dohoonkim.blog.restapi.application.authentication.jwt.JwtService
import kr.dohoonkim.blog.restapi.application.authentication.oauth2.CustomOAuth2UserService
import kr.dohoonkim.blog.restapi.application.authentication.oauth2.HttpCookieOAuth2AuthorizationRepository
import kr.dohoonkim.blog.restapi.application.authentication.oauth2.handler.CustomOAuth2AuthenticationFailureHandler
import kr.dohoonkim.blog.restapi.application.authentication.oauth2.handler.CustomOAuth2AuthenticationSuccessHandler
import kr.dohoonkim.blog.restapi.common.security.exception.JwtAccessDeniedHandler
import kr.dohoonkim.blog.restapi.common.security.exception.JwtEntrypointUnauthorizedHandler
import kr.dohoonkim.blog.restapi.common.security.jwt.JwtAuthenticationFilter
import kr.dohoonkim.blog.restapi.common.security.jwt.JwtAuthenticationProvider
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(
//    private val authenticationConfiguration: AuthenticationConfiguration,
    private val oAuth2AuthorizationRepository: HttpCookieOAuth2AuthorizationRepository,
    private val oAuth2AuthenticationSuccessHandler: CustomOAuth2AuthenticationSuccessHandler,
    private val oAuth2AuthenticationFailureHandler: CustomOAuth2AuthenticationFailureHandler,
    private val customOAuth2UserService: CustomOAuth2UserService,
) {

    private val logger = LoggerFactory.getLogger(this::class.java)


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

////    @Bean
////    fun authenticationManager(
////        jwtAuthenticationProvider: JwtAuthenticationProvider,
////        http: HttpSecurity,
////    ): AuthenticationManager {
////        val defaultAuthenticationManager = http.getSharedObject(AuthenticationManager::class.java)
////        return if (defaultAuthenticationManager != null) {
////            // 기본 AuthenticationManager에 JWT Provider 추가
////            val providers = mutableListOf<AuthenticationProvider>()
////            providers.add(jwtAuthenticationProvider)
////
////            // 기존 providers가 ProviderManager인 경우 기존 providers도 추가
////            if (defaultAuthenticationManager is ProviderManager) {
////                providers.addAll(defaultAuthenticationManager.providers)
////            }
////
////            ProviderManager(providers)
////        } else {
////            // 기본 AuthenticationManager가 없다면 JWT Provider만 사용
////            ProviderManager(jwtAuthenticationProvider)
////        }
//    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
//        authenticationManager: AuthenticationManager,
        jwtEntryPointUnauthorizedHandler: JwtEntrypointUnauthorizedHandler,
        jwtAccessDeniedHandler: JwtAccessDeniedHandler,
        jwtAuthenticationProvider: JwtAuthenticationProvider
    ): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
            .cors {}
            .formLogin { form -> form.disable() }
            .httpBasic { basic -> basic.disable() }
            .sessionManagement {
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
//            .authenticationManager(authenticationManager)
            .addFilterBefore(JwtAuthenticationFilter(ProviderManager(jwtAuthenticationProvider)), UsernamePasswordAuthenticationFilter::class.java )
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
                it.requestMatchers(HttpMethod.POST, "/api/v1/members").permitAll()
                it.requestMatchers("/api/v1/auth/**").permitAll()
                it.requestMatchers("/api/v1/members/**").hasAnyRole("MEMBER", "ADMIN")
                it.requestMatchers("/api/v1/admin/**").hasAnyRole("ADMIN")
                it.requestMatchers("/api/v1/files/**").hasAnyRole("ADMIN")
                it.requestMatchers(HttpMethod.GET, "/api/v1/articles/**", "/api/v1/article-categories/**").permitAll()
                it.anyRequest().authenticated()
            }
            .oauth2Login { customizer ->
                customizer.authorizationEndpoint { config->
//                    authorization.baseUri("/api/v1/authentication/oauth2")
                    config.baseUri("/api/v1/auth/oauth2")
                    config.authorizationRequestRepository(oAuth2AuthorizationRepository)
                }
                customizer.redirectionEndpoint { redirectEndpoint ->
                    redirectEndpoint.baseUri("/api/v1/auth/oauth2/callback/*")
                }
                customizer.userInfoEndpoint {
                    it.userService(customOAuth2UserService)
                }
                customizer.successHandler(oAuth2AuthenticationSuccessHandler)
                customizer.failureHandler(oAuth2AuthenticationFailureHandler)
            }
        return http.build();
    }

}