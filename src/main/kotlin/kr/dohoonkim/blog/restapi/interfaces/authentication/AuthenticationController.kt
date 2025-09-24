package kr.dohoonkim.blog.restapi.interfaces.authentication

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.application.authentication.AuthenticationServiceFacade
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import kr.dohoonkim.blog.restapi.common.utility.CookieUtils
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.EmailPasswordLoginRequest
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.LoginResponse
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.ReissueAccessTokenRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication API", description = "인증 기능 제공")
class AuthenticationController(
    private val authenticationServiceFacade: AuthenticationServiceFacade,
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {
        private const val REFRESH_TOKEN_COOKIE_NAME = "refresh-token"
    }

    @PostMapping("/email-password")
    @ApplicationCode(ResultCode.AUTHENTICATION_SUCCESS)
    fun loginByEmailPassword(
        response: HttpServletResponse,
        @RequestBody request: EmailPasswordLoginRequest
    ): LoginResponse {
        val result = authenticationServiceFacade.loginByEmailPassword(request.email, request.password)
        CookieUtils.setCookie(response, REFRESH_TOKEN_COOKIE_NAME, result.refreshToken, 60 * 60 * 24 * 7)
        return LoginResponse.from(result)
    }

    @PostMapping("/jwt/reissue")
    @ApplicationCode(ResultCode.REISSUE_TOKEN_SUCCESS)
    fun reissueAccessToken(
        req: HttpServletRequest,
        res: HttpServletResponse,
        @RequestBody request: ReissueAccessTokenRequest
    ): LoginResponse {
        val result = try {
            val refreshToken = CookieUtils.getCookie(
                req,
                REFRESH_TOKEN_COOKIE_NAME
            )?.value ?: request.refreshToken
            val reissueResult = authenticationServiceFacade.reissueAccessToken(refreshToken)
            CookieUtils.setCookie(res, REFRESH_TOKEN_COOKIE_NAME, reissueResult.refreshToken, 60 * 60 * 24 * 14)
            reissueResult
        } catch (e: Exception) {
            CookieUtils.delCookie(res, REFRESH_TOKEN_COOKIE_NAME)
            throw e
        }
        return LoginResponse.from(result)
    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(
        @CookieValue("refresh-token") refreshToken: String,
        response: HttpServletResponse
    ) {
        CookieUtils.delCookie(response, REFRESH_TOKEN_COOKIE_NAME)
        authenticationServiceFacade.logout(refreshToken)
    }
}