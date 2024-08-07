package kr.dohoonkim.blog.restapi.config

import kr.dohoonkim.blog.restapi.application.authentication.CustomUserDetailService
import kr.dohoonkim.blog.restapi.application.authentication.oauth2.CustomOAuth2UserService
import kr.dohoonkim.blog.restapi.security.handler.*
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthenticationFilter
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthenticationProvider
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
class SecurityConfig(
    private val entryPointUnauthorizedHandler: EntryPointUnauthorizedHandler,
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val jwtService: JwtService,
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val oAuth2AuthenticationSuccessHandler: CustomOAuth2AuthenticationSuccessHandler,
    private val oAuth2AuthenticationFailureHandler: CustomOAuth2AuthenticationFailureHandler,
    private val customOAuth2UserService: CustomOAuth2UserService,
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    companion object {
        private val WHITELIST_STATIC = arrayOf("/static/css/**", "/static/js/**", "*.ico", "/error")
        private val WHITELIST_SWAGGER =
            arrayOf("/swagger-ui", "/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs", "/api-docs", "/api-docs/**")
    }


    @Bean
    fun jwtExceptionFilter(): JwtExceptionHandlingFilter {
        return JwtExceptionHandlingFilter(entryPointUnauthorizedHandler)
    }

    @Bean
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter(jwtService, jwtAuthenticationProvider)
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it.ignoring().requestMatchers(*WHITELIST_STATIC, *WHITELIST_SWAGGER)
//            it.ignoring().requestMatchers(PathRequest.toH2Console())
        }
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { csrf -> csrf.disable() }
            .cors {}
            .formLogin { form -> form.disable() }
            .httpBasic { basic -> basic.disable() }
            .sessionManagement {
                sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .exceptionHandling { customizer ->
                customizer.accessDeniedHandler(jwtAccessDeniedHandler)
                customizer.authenticationEntryPoint(entryPointUnauthorizedHandler)
            }
            .authorizeHttpRequests { customizer ->
                customizer.requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                customizer.requestMatchers("/api/v1/files/**").hasRole("ADMIN")
                customizer.anyRequest().permitAll()
            }
            .oauth2Login { customizer ->
                customizer.authorizationEndpoint { authorization ->
                    authorization.baseUri("/api/v1/authentication/oauth2")
                }
                customizer.redirectionEndpoint { redirectEndpoint ->
                    redirectEndpoint.baseUri("/api/v1/authentication/callback/*")
                }
                customizer.userInfoEndpoint {
                    it.userService(customOAuth2UserService)
                }
                customizer.successHandler(oAuth2AuthenticationSuccessHandler)
                customizer.failureHandler(oAuth2AuthenticationFailureHandler)
            }
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
        http.addFilterBefore(jwtExceptionFilter(), JwtAuthenticationFilter::class.java)
        return http.build();
    }

}