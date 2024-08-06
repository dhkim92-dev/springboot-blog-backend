package kr.dohoonkim.blog.restapi.units.application.authentication

import com.auth0.jwt.exceptions.TokenExpiredException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.authentication.AuthenticationService
import kr.dohoonkim.blog.restapi.application.authentication.CustomUserDetailService
import kr.dohoonkim.blog.restapi.application.authentication.vo.JwtClaims
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.MEMBER_NOT_FOUND
import kr.dohoonkim.blog.restapi.common.error.exceptions.ExpiredTokenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import kr.dohoonkim.blog.restapi.support.createMember
import kr.dohoonkim.blog.restapi.support.security.createExpiredRefreshToken
import kr.dohoonkim.blog.restapi.support.security.createJwtConfig
import kr.dohoonkim.blog.restapi.support.security.createJwtService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

internal class AuthenticationServiceTest: BehaviorSpec({

    val jwtService = createJwtService(createJwtConfig(10000, 10000000))
    val memberRepository = mockk<MemberRepository>()
    val passwordEncoder = BCryptPasswordEncoder(10)
    var member = createMember(1).first()
    val authenticationService = AuthenticationService(
        jwtService=jwtService,
        memberRepository = memberRepository,
        passwordEncoder = passwordEncoder
    )

    beforeSpec {
    }

    Given("존재하지 않는 사용자 이메일과 패스워드가 주어진다") {
        val email = "not-exist"
        val password = "test1234"
        When("로그인하면") {
            every { memberRepository.findByEmail(email) } throws NotFoundException(MEMBER_NOT_FOUND)
            Then("NotFoundException이 발생한다") {
                shouldThrow<NotFoundException> {
                    authenticationService.login(email, password)
                }.message shouldBe MEMBER_NOT_FOUND.message
            }
        }
    }

    Given("사용자 이메일과 잘못된 비밀번호가 주어진다") {
        val password = "invalid-password"

        When("로그인하면") {
            every {memberRepository.findByEmail(member.email)} returns member
            Then("BadCredentialsException이 발생한다") {
                shouldThrow<BadCredentialsException> {
                    authenticationService.login(member.email, password)
                }.message shouldBe "email/password mismatched."
            }
        }
    }

    Given("활성화 되지 않은 사용자의 이메일과 비밀번호가 주어진다") {
        member.isActivated = false
        every { memberRepository.findByEmail(member.email) } returns member

        When("로그인하면") {
            Then("UnauthorizedException이 발생한다") {
                shouldThrow<UnauthorizedException> {
                    authenticationService.login(member.email, "test1234")
                }
            }
        }
    }

    Given("정상 유저의 사용자 이메일과 패스워드가 주어진다") {
        When("로그인하면") {
            member.isActivated = true
            every { memberRepository.findByEmail(member.email) } returns member
            Then("Refresh Token과 Access Token이 발급된다") {
                val result = authenticationService.login(member.email, "test1234")
                result.refreshToken.isEmpty() shouldBe false
                result.accessToken.isEmpty() shouldBe false
            }
        }
    }

    Given("유효한 Refresh Token이 주어진다") {
        val jwtClaims = JwtClaims.fromCustomUserDetails(CustomUserDetails.from(member))
        val refreshToken = jwtService.createRefreshToken(jwtClaims)
        every { memberRepository.findByMemberId(member.id) } returns member

        When("유효한 Access Token 재발급 요청하면") {
            val result = authenticationService.reIssueAccessToken(refreshToken)
            Then("Access Token이 발급된다") {
                result.accessToken.isEmpty() shouldBe false
            }
        }
    }

    Given("만료된 Refresh Token이 주어진다") {
        val jwtClaims = JwtClaims.fromCustomUserDetails(CustomUserDetails.from(member))
        val refreshToken = createExpiredRefreshToken(jwtClaims)
        When("Access Token 재발급 요청하면") {
            Then("ExpiredTokenException이 발생한다") {
                shouldThrow<TokenExpiredException> {
                    authenticationService.reIssueAccessToken(refreshToken)
                }
            }
        }
    }

    Given("탈퇴한 사용자의 Refresh Token이 주어진다") {
        val jwtClaims = JwtClaims.fromCustomUserDetails(CustomUserDetails.from(member))
        val refreshToken = jwtService.createRefreshToken(jwtClaims)
        every{ memberRepository.findByMemberId(member.id) } throws NotFoundException(MEMBER_NOT_FOUND)
        When("Access Token 재발급 요청을 하면") {
            Then("NotFoundException이 발생한다") {
                shouldThrow<NotFoundException> {
                    authenticationService.reIssueAccessToken(refreshToken)
                }.message shouldBe MEMBER_NOT_FOUND.message
            }
        }
    }

    afterSpec {
        clearAllMocks()
    }
})