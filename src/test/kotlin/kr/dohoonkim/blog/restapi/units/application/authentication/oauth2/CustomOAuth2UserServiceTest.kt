package kr.dohoonkim.blog.restapi.units.application.authentication.oauth2

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import kr.dohoonkim.blog.restapi.application.authentication.oauth2.CustomOAuth2UserService
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.support.entity.createMember
import kr.dohoonkim.blog.restapi.support.security.oauth2.GithubOAuth2User
import kr.dohoonkim.blog.restapi.support.security.oauth2.createGithubAttribute
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User

internal class CustomOAuth2UserServiceTest: BehaviorSpec({
    val memberRepository = mockk<MemberRepository>()
    val passwordEncoder = BCryptPasswordEncoder(10)
    val delegate = mockk<DefaultOAuth2UserService>()
    val userService = CustomOAuth2UserService(memberRepository, passwordEncoder as PasswordEncoder, delegate)
    val userRequest = mockk<OAuth2UserRequest>()
    val githubOAuth2User = GithubOAuth2User(createGithubAttribute())
    val member = createMember(1).first()

    beforeSpec {

    }

    Given("신규 가입자의 OAuth2UserRequest가 주어진다") {
        every { delegate.loadUser(any()) } returns githubOAuth2User
        every { userRequest.clientRegistration.registrationId } returns "github"
        every { memberRepository.existsByEmail(any()) } returns false
        every { memberRepository.existsByNickname(any()) } returns false
        member.updateNickname("github:${githubOAuth2User.name}")
        every { memberRepository.save(any()) } returns member

        When("OAuth2 로그인을 시도하면") {
            val result = userService.loadUser(userRequest)
            Then("OAuth2 User가 반환된다") {
                result.name shouldBe member.nickname
            }
        }
    }

    Given("이미 가입한 사용자의 OAuth2UserRequest가 주어진다") {
        every { delegate.loadUser(any()) } returns githubOAuth2User
        every { userRequest.clientRegistration.registrationId } returns "github"
        every { memberRepository.existsByEmail(any()) } returns true
        every { memberRepository.findByEmail(any()) } returns member
        member.updateNickname("github:${githubOAuth2User.name}")
        every { memberRepository.save(any()) } returns member
        When("로그인을 하면") {
            val result = userService.loadUser(userRequest)
            Then("OAuth2User가 반환된다") {
                result.name shouldBe member.nickname
            }
        }
    }

    afterSpec {
        clearAllMocks()
    }
})

