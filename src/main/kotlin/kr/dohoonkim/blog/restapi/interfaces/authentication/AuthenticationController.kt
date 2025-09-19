package kr.dohoonkim.blog.restapi.interfaces.authentication

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.application.authentication.AuthenticationServiceFacade
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import kr.dohoonkim.blog.restapi.common.utility.CookieUtils
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.EmailPasswordLoginRequest
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.LoginResponse
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.ReissueAccessTokenRequest
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

    @PostMapping("/email-password")
    @ApplicationCode(ResultCode.AUTHENTICATION_SUCCESS)
    fun loginByEmailPassword(
        response: HttpServletResponse,
        @RequestBody request: EmailPasswordLoginRequest
    ): LoginResponse {
        val result = authenticationServiceFacade.loginByEmailPassword(request.email, request.password)
        CookieUtils.setCookie(response, "refresh-token", result.refreshToken, 60 * 60 * 24 * 14)
        return LoginResponse.from(result)
    }

    @PostMapping("/jwt/reissue")
    @ApplicationCode(ResultCode.REISSUE_TOKEN_SUCCESS)
    fun reissueAccessToken(
        response: HttpServletResponse,
        @RequestBody request: ReissueAccessTokenRequest
    ): LoginResponse {
        val result = authenticationServiceFacade.reissueAccessToken(response, request.refreshToken)
        return LoginResponse.from(result)
    }

    @DeleteMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(
        @CookieValue("refresh-token") refreshToken: String,
        response: HttpServletResponse
    ) {
        CookieUtils.delCookie(response, "refresh-token")
        authenticationServiceFacade.logout(refreshToken)
    }
}