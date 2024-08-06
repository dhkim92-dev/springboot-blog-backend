package kr.dohoonkim.blog.restapi.units.interfaces.member

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.member.MemberService
import kr.dohoonkim.blog.restapi.application.member.dto.MemberDto
import kr.dohoonkim.blog.restapi.common.response.PageList
import kr.dohoonkim.blog.restapi.domain.member.Role
import kr.dohoonkim.blog.restapi.interfaces.member.MemberController
import kr.dohoonkim.blog.restapi.interfaces.member.dto.EmailChangeRequest
import kr.dohoonkim.blog.restapi.interfaces.member.dto.MemberJoinRequest
import kr.dohoonkim.blog.restapi.interfaces.member.dto.NicknameChangeRequest
import kr.dohoonkim.blog.restapi.interfaces.member.dto.PasswordChangeRequest
import kr.dohoonkim.blog.restapi.support.entity.createMember
import org.springframework.data.domain.PageRequest

internal class MemberControllerTest: BehaviorSpec({

    val memberService = mockk<MemberService>()
    val memberController = MemberController(memberService)

    val admin = createMember(Role.ADMIN, true)
    val member = createMember(Role.MEMBER, true)

    Given("회원가입 요청이 주어진다") {
        val request = MemberJoinRequest(
            nickname = member.nickname,
            email = member.email,
            password = "test1234"
        )

        When("요청이 성공하면") {
            every { memberService.create(any()) } returns MemberDto.fromEntity(member)
            val result = memberController.createMember(request)
            Then("회원가입 된다") {
                result.email shouldBe request.email
                result.nickname shouldBe result.nickname
            }
        }
    }

    Given("회원 닉네임 변경 요청이 주어진다") {
        val request = NicknameChangeRequest(nickname = "newnickname")
        member.updateNickname(request.nickname)
        When("요청이 처리되면") {
            every { memberService.changeMemberNickname(any(), any()) } returns MemberDto.fromEntity(member)
            Then("닉네임이 변경된다") {
                val result = memberController.changeNickname(request, member.id, member.id)
                result.nickname shouldBe request.nickname
            }
        }
    }

    Given("회원 비밀번호 변경 요청이 주어진다") {
        val request = PasswordChangeRequest(currentPassword = "test1234", newPassword="newpassword")
        When("요청이 처리되면") {
            member.updatePassword(request.newPassword)
            every {memberService.changePassword(member.id, any())} returns MemberDto.fromEntity(member)

            Then("비밀번호가 변경된다") {
                val result = memberController.changePassword(member.id, member.id, request)
                result.id shouldBe member.id
            }
        }
    }

    Given("이메일 변경 요청이 주어진다") {
        val request = EmailChangeRequest(email="newemail@dohoon-kim.kr")
        When("요청이 처리되면") {
            member.updateEmail(request.email)
            every { memberService.changeMemberEmail(member.id, any()) } returns MemberDto.fromEntity(member)
            Then("이메일이 변경된다") {
                val result = memberController.changeEmail(member.id, member.id, request)
                result.email shouldBe request.email
            }
        }
    }

    Given("회원 탈퇴 요청이 주어진다") {
        val memberId = member.id
        When("요청이 처리되면") {
            every { memberService.delete(memberId, memberId) } returns Unit
            Then("회원 탈퇴된다") {
                val result = memberController.withdrawal(memberId, memberId)
                result shouldBe Unit
            }
        }
    }

    Given("회원 목록 조회 요청이 주어진다") {
        val members = createMember(20)
        val pageRequest: PageRequest = PageRequest.of(0, 20)

        When("요청이 처리되면") {

            every { memberService.getMembers(any()) } returns PageList.of(
                data = List(20){MemberDto.fromEntity(members[it])},
                total = 20,
                pageable = pageRequest
            )

            Then("회원 목록이 조회된다") {
                val result = memberController.getMembers(pageRequest)

                result.count shouldBe 20
                result.total shouldBe 20
                result.data.forEachIndexed { index, memberDto ->
                    memberDto.id shouldBe members[index].id
                    memberDto.nickname shouldBe members[index].nickname
                    memberDto.email shouldBe members[index].email
                    memberDto.role shouldBe members[index].role.rolename
                    memberDto.isActivated shouldBe members[index].isActivated
                }
            }
        }
    }

    Given("이메일 중복확인 요청이 주어진다") {
        val email = "check@dohoon-kim.kr"

        When("중복되지 않으면") {
            every { memberService.checkEmailAvailable(email) } returns true
            Then("true가 반환된다") {
                memberController.checkEmail(email) shouldBe true
            }
        }
    }

    Given("닉네임 중복확인 요청이 주어진다") {
        val nickname = "nickname"
        When("중복되지 않으면") {
            every { memberService.checkNicknameAvailable(nickname) } returns true
            Then("true가 반환된다") {
                memberController.checkNickname(nickname) shouldBe true
            }
        }
    }


    afterSpec {
        clearAllMocks()
    }
})