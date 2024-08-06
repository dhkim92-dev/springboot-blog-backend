package kr.dohoonkim.blog.restapi.units.application.authentication

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.authentication.CustomUserDetailService
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.*
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.support.entity.createMember
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

internal class CustomUserDetailServiceTest: BehaviorSpec({

    val member = createMember(1).first()
    val memberRepository = mockk<MemberRepository>()
    val customUserDetailService = CustomUserDetailService(memberRepository)

    fun checkEqual(member: Member, detail: CustomUserDetails) {
        member.id shouldBe detail.memberId
        member.email shouldBe detail.email
        member.role shouldBe detail.role
        member.nickname shouldBe detail.nickname
        member.isActivated shouldBe detail.isActivated
    }

    Given("사용자 email이 주어진다") {
        When("사용자가 존재하면") {
            every { memberRepository.findByEmail(member.email) } returns member

            Then("UserDetail 이 반환된다") {
                val result = customUserDetailService.loadUserByUsername(member.email)
                (result is UserDetails) shouldBe true
                checkEqual(member, result as CustomUserDetails)
            }
        }

        When("사용자가 존재하지 않으면") {
            every { memberRepository.findByEmail(member.email) } returns null
            Then("NotFoundException이 발생한다") {
                shouldThrow<NotFoundException> {
                    customUserDetailService.loadUserByUsername(member.email)
                }.message shouldBe MEMBER_NOT_FOUND.message
            }
        }
    }

    Given("UserDetails와 임의의 문자열이 비밀번호로 주어진다") {
        val userDetail = CustomUserDetails.from(member)
        val password = ""

        When("사용자가 존재하지 않으면") {
            every { memberRepository.findByEmail(any()) } returns null

            Then("NotFoundException이 발생한다") {
                shouldThrow<NotFoundException> {
                    customUserDetailService.updatePassword(userDetail, password)
                }.message shouldBe MEMBER_NOT_FOUND.message
            }
        }

        When("사용자가 존재하면") {
            every { memberRepository.findByEmail(any()) } returns member
            Then("UserDetails로 반환된다") {
                val result = customUserDetailService.updatePassword(userDetail, password)
                (result is CustomUserDetails) shouldBe true
            }
        }
    }
})