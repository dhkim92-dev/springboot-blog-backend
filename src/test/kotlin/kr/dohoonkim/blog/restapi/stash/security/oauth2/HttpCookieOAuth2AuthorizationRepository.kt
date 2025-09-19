package kr.dohoonkim.blog.restapi.stash.security.oauth2

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.common.utility.CookieUtils
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils

@Component
class HttpCookieOAuth2AuthorizationRepository: AuthorizationRequestRepository<OAuth2AuthorizationRequest>{

    companion object {
        val OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2-auth-request"
        val REDIRECT_URI_PARAM_COOKIE_NAME = "redirect-uri"
        val MODE_PARAM_COOKIE_NAME = "mode"
        val COOKIE_EXPIRE_SECONDS = 180
    }

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        val cookies = request.cookies ?: arrayOf<Cookie>()

        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)?.let{ cookie ->
                CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest::class.java)
            }
    }

    override fun removeAuthorizationRequest(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): OAuth2AuthorizationRequest? {
        return this.loadAuthorizationRequest(request)
    }

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        logger.debug("saveAuthorizationRequest called")
        if(authorizationRequest == null) {
            CookieUtils.delCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
            CookieUtils.delCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME)
            CookieUtils.delCookie(response, MODE_PARAM_COOKIE_NAME)
            return
        }

        CookieUtils.setCookie(
            response,
            OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
            CookieUtils.serialize(authorizationRequest),
            COOKIE_EXPIRE_SECONDS
        )

        val redirectAfterLoginSuccess = authorizationRequest.redirectUri
        if(StringUtils.hasText(redirectAfterLoginSuccess)) {
            CookieUtils.setCookie(
                response,
                REDIRECT_URI_PARAM_COOKIE_NAME,
                redirectAfterLoginSuccess,
                COOKIE_EXPIRE_SECONDS
            )
        }

        val mode = request.getParameter(MODE_PARAM_COOKIE_NAME)
        if(StringUtils.hasText(mode)) {
            CookieUtils.setCookie(
                response,
                MODE_PARAM_COOKIE_NAME,
                mode,
                COOKIE_EXPIRE_SECONDS
            )
        }
    }

    fun removeAuthorizationRequestCookies(response: HttpServletResponse) {
        CookieUtils.delCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
        CookieUtils.delCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME)
        CookieUtils.delCookie(response, MODE_PARAM_COOKIE_NAME)
    }
}