package kr.dohoonkim.blog.restapi.security.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.application.authentication.CustomUserDetailService
import kr.dohoonkim.blog.restapi.application.authentication.dto.JwtClaims
import kr.dohoonkim.blog.restapi.application.authentication.dto.MemberProfile
import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.util.UriComponentsBuilder

@Component
class CustomOAuth2AuthenticationSuccessHandler(
    @Value("\${oauth2.redirect-uri.on-success}")
    private val redirectUri : String,
    private val jwtService: JwtService,
    private val customUserDetailService: CustomUserDetailService
) : SimpleUrlAuthenticationSuccessHandler() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        logger.debug("request values : ${request.requestURI}")
        logger.debug("request url : ${request.requestURL}")
        logger.debug("request params : ${request.queryString}")
        val oAuth2User = authentication.principal as MemberProfile
//        logger.debug("oAuth2User name : ${oAuth2User.name}")
//        logger.debug("oAuth2User real name : ${oAuth2User.attributes["name"]}")
//        logger.debug("oAuth2User email address : ${oAuth2User.attributes["email"]}")
        logger.debug("oAuth2User email verified: ${oAuth2User.attributes["email_verified"]}")
        logger.debug("oAuth2User token : ${oAuth2User.attributes["access_token"]}")
        logger.debug("oAuth2User attributes : ${oAuth2User.attributes.toString()}")

        val userDetail = customUserDetailService.loadUserByUsername(oAuth2User.email) as CustomUserDetails
        val claims = JwtClaims.fromCustomUserDetails(userDetail)
        val accessToken = jwtService.createAccessToken(claims)
        val refreshToken = jwtService.createRefreshToken(claims)

       redirectStrategy.sendRedirect(request, response, getRedirectUrl(accessToken, refreshToken))
    }

    private fun getRedirectUrl(accessToken : String, refreshToken : String) : String {
        return UriComponentsBuilder.fromUriString(redirectUri)
            .queryParam("refresh-token", refreshToken)
            .queryParam("access-token", accessToken)
            .build()
            .toUriString()
    }
}