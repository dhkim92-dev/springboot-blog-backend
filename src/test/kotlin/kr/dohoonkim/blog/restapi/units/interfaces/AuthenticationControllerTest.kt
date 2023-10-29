package kr.dohoonkim.blog.restapi.units.interfaces

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.authentication.AuthenticationService
import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginRequest
import kr.dohoonkim.blog.restapi.application.authentication.dto.ReissueResult
import kr.dohoonkim.blog.restapi.application.authentication.dto.ReissueTokenRequest
import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse
import kr.dohoonkim.blog.restapi.common.error.exceptions.JwtInvalidException
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.ResultCode.AUTHENTICATION_SUCCESS
import kr.dohoonkim.blog.restapi.common.response.ResultCode.REISSUE_TOKEN_SUCCESS
import kr.dohoonkim.blog.restapi.interfaces.AuthenticationController
import kr.dohoonkim.blog.restapi.support.web.createLoginResult
import kr.dohoonkim.blog.restapi.support.web.createLoginResultResponse
import kr.dohoonkim.blog.restapi.support.entity.createMember
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class AuthenticationControllerTest : AnnotationSpec() {

    private val passwordEncoder = BCryptPasswordEncoder()
    private val user = createMember()
    private val authenticationService = mockk<AuthenticationService>()
    private val authenticationController = AuthenticationController(authenticationService)

    @Test
    fun `사용자가 로그인한다`() {
        val loginResult = createLoginResult(user)
        val request = LoginRequest(email = user.email, password = user.password)

        every { authenticationService.login(any()) } returns loginResult

        val response = authenticationController.login(request).body!!
        response.status shouldBe OK.value()
        response.code shouldBe AUTHENTICATION_SUCCESS.code
        response.data shouldBe loginResult
    }

    @Test
    fun `사용자가 잘못된 정보로 로그인을 한다`() {
        val request = LoginRequest(email = user.email, password = "invalid password")
        val expected = ErrorResponse.of(ErrorCode.AUTHENTICATION_FAIL)

        every { authenticationService.login(any()) } throws BadCredentialsException("email/password mismatched.")

        shouldThrow<BadCredentialsException> {
            authenticationController.login(request)
        }
    }

    @Test
    fun `사용자가 Access Token을 재발급한다`() {
        val refreshToken = createLoginResult(user).refreshToken
        val reissueResult = ReissueResult(accessToken = createLoginResult(user).accessToken)
        val request = ReissueTokenRequest(refreshToken = refreshToken)

        every { authenticationService.reIssueAccessToken(any()) } returns reissueResult

        val response = authenticationController.reissue(request).body!!
        response.status shouldBe REISSUE_TOKEN_SUCCESS.status.value()
        response.code shouldBe REISSUE_TOKEN_SUCCESS.code
        response.data shouldBe reissueResult
    }

    @Test
    fun `사용자가 잘못된 Refresh Token으로 재발급을 요청한다`() {
        val error = ErrorResponse.of(ErrorCode.AUTHENTICATION_FAIL)
        val request = ReissueTokenRequest(refreshToken = "")

        every { authenticationService.reIssueAccessToken(any()) } throws UnauthorizedException()

        shouldThrow<UnauthorizedException> {
            authenticationController.reissue(request).body!!
        }
    }

    @AfterEach
    fun `clearnMockks`() {
        clearAllMocks()
    }

}