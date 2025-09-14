package kr.dohoonkim.blog.restapi.units.interfaces.authentication

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.authentication.AuthenticationService
import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginResult
import kr.dohoonkim.blog.restapi.application.authentication.dto.ReissueResult
import kr.dohoonkim.blog.restapi.interfaces.authentication.AuthenticationController
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.LoginRequest
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.ReissueResponse
import kr.dohoonkim.blog.restapi.interfaces.authentication.dto.ReissueTokenRequest

class AuthenticationControllerTest: BehaviorSpec({

    val authenticationService = mockk<AuthenticationService>()
    val authenticationController = AuthenticationController(authenticationService)

    Given("Email/Password Login Request가 주어진다") {
        val request = LoginRequest(
            email="admin@dohoon-kim.kr",
            password="test1234"
        )

        every { authenticationService.login(request.email, request.password ) } returns LoginResult(refreshToken = "refresh-token", accessToken = "access-token")
        When("로그인 요청을 보내면") {
            Then("Refresh/Access Token이 발급된다") {
                val response = authenticationController.login(request)
                response.type shouldBe "Bearer"
                response.accessToken shouldBe "access-token"
                response.refreshToken shouldBe "refresh-token"
            }
        }
    }

    Given("Access Token 재발급 Request가 주어진다") {
        val request = ReissueTokenRequest(refreshToken = "refresh-token")
        When("요청이 성공하면") {
            every { authenticationService.reIssueAccessToken(request.refreshToken) } returns ReissueResult(type="Bearer", accessToken = "access-token")
            val response = authenticationController.reissue(request)
            Then("Access Token이 재발급 된다") {
                (response is ReissueResponse) shouldBe true
                response.type shouldBe "Bearer"
                response.accessToken shouldBe "access-token"
            }
        }
    }
})