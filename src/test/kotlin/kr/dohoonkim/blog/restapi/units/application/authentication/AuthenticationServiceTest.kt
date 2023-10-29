package kr.dohoonkim.blog.restapi.units.application.authentication

import com.auth0.jwt.exceptions.JWTDecodeException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.authentication.AuthenticationService
import kr.dohoonkim.blog.restapi.application.authentication.CustomUserDetailService
import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginRequest
import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginResult
import kr.dohoonkim.blog.restapi.application.authentication.dto.ReissueResult
import kr.dohoonkim.blog.restapi.application.authentication.dto.ReissueTokenRequest
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.support.entity.createMember
import kr.dohoonkim.blog.restapi.units.utility.JwtServiceTest.Companion.jwtConfig
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class AuthenticationServiceTest : BehaviorSpec({
    val jwtConfig = jwtConfig
    val jwtService = JwtService(jwtConfig)
    val passwordEncoder = BCryptPasswordEncoder()
    val memberRepository = mockk<MemberRepository>()
    val userDetailService = mockk<CustomUserDetailService>()
    val authenticationService = AuthenticationService(jwtService,
        memberRepository,
        userDetailService,
        passwordEncoder
    )
    val user = createMember()
    user.updatePassword(passwordEncoder.encode("test"))
    val validLoginRequest = LoginRequest(
        email = user.email,
        password = "test"
    )
    every { userDetailService.loadUserByUsername(any()) } returns CustomUserDetails.from(user)

    Given("로그인을 한다.") {
        When("아이디와 비밀번호가 일치하면") {
            Then("refresh/access 토큰이 발급된다.") {
                val ret = authenticationService.login(validLoginRequest)
                ret::class shouldBe LoginResult::class
            }
        }

        When("아이디와 비밀번호가 일치하지 않으면") {
            val invalidRequet = LoginRequest(
                email = user.email,
                password = "invalid"
            )

            Then("에러가 발생한다.") {
                shouldThrow<BadCredentialsException> {
                    authenticationService.login(invalidRequet)
                }
            }
        }
    }

    Given("Access Token을 재발급한다.") {
        val tokens = authenticationService.login(validLoginRequest)

        When("Refresh Token 유효하면") {
            every { memberRepository.findByMemberId(any()) } returns user
            every { userDetailService.loadUserByUsername(any()) } returns CustomUserDetails.from(user)

            Then("Token이 발급된다.") {
                val request = ReissueTokenRequest(refreshToken = tokens.refreshToken)
                val ret = authenticationService.reIssueAccessToken(request)

                ret::class shouldBe ReissueResult::class
            }
        }

        When("Refresh Token이 유효하지 않으면") {
            val request = ReissueTokenRequest("invalid-token")

            Then("에러가 발생한다.") {
                shouldThrow<JWTDecodeException> {
                    authenticationService.reIssueAccessToken(request)
                }
            }
        }
    }

    afterSpec{ clearAllMocks() }

})