package kr.dohoonkim.blog.restapi.interfaces.authentication

import com.auth0.jwt.exceptions.TokenExpiredException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import kr.dohoonkim.blog.restapi.application.authentication.AuthenticationService
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.LoginRequest
import kr.dohoonkim.blog.restapi.common.response.ResultCode.AUTHENTICATION_SUCCESS
import kr.dohoonkim.blog.restapi.common.response.ResultCode.REISSUE_TOKEN_SUCCESS
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import kr.dohoonkim.blog.restapi.common.utility.CookieUtils
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.LoginResponse
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.ReissueResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NO_CONTENT
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/")
@Tag(name = "Authentication API", description = "사용자 인증을 제공합니다.")
class AuthenticationController(
    private val authenticationService: AuthenticationService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("v1/authentication")
    @Operation(summary = "Email/Password 인증", description = "이메일과 비밀번호로 로그인하여 토큰을 획득한다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "인증 성공"),
            ApiResponse(
                responseCode = "401",
                description = "A001 - 로그인 실패<br/>",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @ApplicationCode(AUTHENTICATION_SUCCESS)
    fun login(@RequestBody @Valid request: LoginRequest, response: HttpServletResponse): LoginResponse {
        val result = this.authenticationService.login(request.email, request.password)
        CookieUtils.setCookie(response, "refresh-token", result.refreshToken, result.refreshTokenExpiry)
        return LoginResponse.valueOf(result)
    }

    @DeleteMapping("v1/authentication/revoke")
    @ResponseStatus(NO_CONTENT)
    fun revokeJWT(response: HttpServletResponse) {
        CookieUtils.setCookie(response, "refresh-token", "", 0)
    }

    @PostMapping("v1/authentication/reissue")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "A002 - 토큰 재발급 성공"),
            ApiResponse(
                responseCode = "401", description = "J001 - 잘못된 토큰<br/>J002 - 만료된 리프레시 토큰<br/>J003 - 잘못된 인증 헤더",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @ApplicationCode(REISSUE_TOKEN_SUCCESS)
    fun reissue(request: HttpServletRequest, response: HttpServletResponse): ReissueResponse {
        val refreshToken = CookieUtils.getCookie(request, "refresh-token")?.value
            ?: throw UnauthorizedException(ErrorCodes.REFRESH_TOKEN_MISSING)

        try {
            val result = authenticationService.reIssueAccessToken(refreshToken)
            return ReissueResponse.valueOf(result)
        }catch(e: TokenExpiredException) {
            CookieUtils.setCookie(response, "refresh-token", "", 0)
            throw e
        }
    }
}