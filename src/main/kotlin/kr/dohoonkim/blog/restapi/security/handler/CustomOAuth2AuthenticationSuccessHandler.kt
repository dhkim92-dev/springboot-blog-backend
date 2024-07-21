package kr.dohoonkim.blog.restapi.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.application.authentication.CustomUserDetailService
import kr.dohoonkim.blog.restapi.application.authentication.dto.JwtClaims
import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginResult
import kr.dohoonkim.blog.restapi.application.authentication.dto.MemberProfile
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.util.UriComponentsBuilder

@Component
class CustomOAuth2AuthenticationSuccessHandler(
    @Value("\${oauth2.redirect-uri.on-success}")
    private val redirectUri: String,
    private val jwtService: JwtService,
    private val customUserDetailService: CustomUserDetailService,
    private val objectMapper: ObjectMapper
) : SimpleUrlAuthenticationSuccessHandler() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as MemberProfile
        val userDetail = customUserDetailService.loadUserByUsername(oAuth2User.email) as CustomUserDetails
        val claims = JwtClaims.fromCustomUserDetails(userDetail)
        val accessToken = jwtService.createAccessToken(claims)
        val refreshToken = jwtService.createRefreshToken(claims)
        val body = ApiResult.Ok(ResultCode.AUTHENTICATION_SUCCESS, LoginResult(refreshToken=refreshToken, accessToken = accessToken))
        //redirectStrategy.sendRedirect(request, response, getRedirectUrl(accessToken, refreshToken))

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = 200
        response.writer.write(objectMapper.writeValueAsString(body))
        response.writer.flush()
    }

    private fun getRedirectUrl(accessToken: String, refreshToken: String): String {
        return UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("refresh-token", refreshToken)
            .queryParam("access-token", accessToken)
            .build()
            .toUriString()
    }
}