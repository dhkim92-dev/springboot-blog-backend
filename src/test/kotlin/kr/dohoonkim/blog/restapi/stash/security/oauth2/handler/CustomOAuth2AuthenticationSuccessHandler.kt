package kr.dohoonkim.blog.restapi.stash.security.oauth2.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.security.oauth2.OAuth2MemberService
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.utility.CookieUtils
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.LoginResponse
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import kr.dohoonkim.blog.restapi.security.oauth2.HttpCookieOAuth2AuthorizationRepository
import kr.dohoonkim.blog.restapi.security.oauth2.OAuth2UserPrincipal
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import kr.dohoonkim.blog.restapi.security.oauth2.HttpCookieOAuth2AuthorizationRepository.Companion.MODE_PARAM_COOKIE_NAME

@Component
class CustomOAuth2AuthenticationSuccessHandler(
    private val httpCookieOAuth2AuthorizationRepository: HttpCookieOAuth2AuthorizationRepository,
    private val oAuth2MemberService: OAuth2MemberService,
    private val jwtService: JwtService,
    private val objectMapper: ObjectMapper
) : SimpleUrlAuthenticationSuccessHandler() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val principal = getPrincipal(authentication)
//        val redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)?.value ?: throw UnauthorizedException(ErrorCodes.OAUTH2_REDIRECT_URL_RESOLVE_FAILED)
        val mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)?.value ?: "sign-in"

        try {
            if(mode == "sign-in"){
                processSignInMode(principal, request,response)
            }
        } catch(e: Exception) {
            throw e
        } finally {
            clearAuthenticationRequestAttributes(request, response)
        }
    }

    private fun processSignInMode(principal: OAuth2UserPrincipal, request: HttpServletRequest, response: HttpServletResponse) {
        val member = oAuth2MemberService.getOrJoin(principal.getProfile(), principal.getToken())
        val profile = principal.getProfile()
        logger.info("member ${member.nickname} login using oauth2 provider = ${profile.provider.providerName} userId: ${profile.id} token : ${principal.getToken()}")

        val accessToken = jwtService.createAccessToken(member)
        val refreshToken = jwtService.createRefreshToken(member)
        val result = ApiResult.Ok(ResultCode.AUTHENTICATION_SUCCESS, LoginResponse(accessToken = accessToken))
        response.contentType = "application/json; charset=utf-8"
        response.status = 201
        clearAuthenticationRequestAttributes(request, response)
        CookieUtils.setCookie(response, "refresh-token", refreshToken, 3600*24*7)
        response.writer.write(objectMapper.writeValueAsString(result))
        response.writer.flush()
        response.writer.close()
    }

    private fun getPrincipal(authentication: Authentication): OAuth2UserPrincipal {
        val principal = authentication.principal?.let {
            it as OAuth2UserPrincipal
        } ?: throw UnauthorizedException(ErrorCodes.FAIL_TO_ACQUIRE_USER_INFO)
        return principal
    }

    private fun clearAuthenticationRequestAttributes(request: HttpServletRequest, response: HttpServletResponse) {
        super.clearAuthenticationAttributes(request)
        httpCookieOAuth2AuthorizationRepository.removeAuthorizationRequestCookies(response)
    }
}