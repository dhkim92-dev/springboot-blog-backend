package kr.dohoonkim.blog.restapi.units.application.member

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.member.MemberService
import kr.dohoonkim.blog.restapi.application.member.dto.*
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.ALREADY_EXIST_NICKNAME
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.RESOURCE_OWNERSHIP_VIOLATION
import kr.dohoonkim.blog.restapi.common.error.exceptions.ConflictException
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import kr.dohoonkim.blog.restapi.common.response.PageList
import kr.dohoonkim.blog.restapi.common.utility.AuthenticationUtil
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.support.entity.createMember
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

internal class MemberServiceTest: BehaviorSpec({
    val passwordEncoder = BCryptPasswordEncoder(10)
    val authenticationUtil = mockk<AuthenticationUtil>(relaxed = true)
    val memberRepository = mockk<MemberRepository>(relaxed = true)
    val memberService = MemberService(memberRepository, passwordEncoder, authenticationUtil)
    var member = createMember(1).first()
    var memberPassword: String ="test1234"
    var memberId= member.id

    fun checkEqual(dto: MemberDto, entity: Member) {
        dto.id shouldBe entity.id
        dto.email shouldBe entity.email
        dto.role shouldBe entity.role.rolename
        dto.isActivated shouldBe entity.isActivated
        dto.nickname shouldBe entity.nickname
    }

    beforeSpec {
        member.id = UUID.randomUUID()
        memberId = member.id
        memberPassword = member.password
        member.updateEmail(passwordEncoder.encode(member.password))
    }

    Given("중복 확인을 하려는 닉네임이 주어진다") {

        When("중복되지 않으면") {
            every { memberRepository.existsByNickname(any()) } returns false
            Then("true가 반환된다") {
                memberService.checkNicknameAvailable("nickname") shouldBe true
            }
        }

        When("중복되면") {
            every { memberRepository.existsByNickname(any()) } returns true
            Then("ConflictException이 발생한다") {
                shouldThrow<ConflictException> {
                    memberService.checkNicknameAvailable("nickname")
                }.message shouldBe ALREADY_EXIST_NICKNAME.message
            }
        }
    }

    Given("중복 확인을 하려는 이메일이 주어진다") {
        When("중복되지 않으면") {
            every { memberRepository.existsByEmail(any()) } returns false
            Then("true가 반환된다") {
                memberService.checkEmailAvailable(member.email) shouldBe true
            }
        }

        When("중복되면") {
            every { memberRepository.existsByEmail(any()) } returns true

            Then("Conflict Exception이 발생한다") {
                shouldThrow<ConflictException> {
                    memberService.checkEmailAvailable(member.email)
                }.message shouldBe ErrorCodes.ALREADY_EXIST_EMAIL.message
            }
        }
    }

    Given("회원 가입 커맨드가 주어진다") {
        val command = MemberCreateCommand(
            nickname = member.nickname,
            password = member.password,
            email = member.email
        )
        every { memberRepository.save( any()) } returns member
        every { memberRepository.existsByEmail(any()) } returns false
        every { memberRepository.existsByNickname(any()) } returns false

        When("이메일이 중복되지 않고, 닉네임도 중복되지 않으면") {
            Then("가입된다.") {
                val result = memberService.create(command)
                (result is MemberDto) shouldBe true
                checkEqual(result, member)
            }
        }

        When("이메일이 중복되면") {
            every { memberRepository.existsByEmail(command.email) } returns true
            Then("ConflictException이 발생한다") {
                shouldThrow<ConflictException> {
                    memberService.create(command)
                }.message shouldBe ErrorCodes.ALREADY_EXIST_EMAIL.message
            }
        }

        When("닉네임이 중복되면") {
            every { memberRepository.existsByEmail(any()) } returns false
            every { memberRepository.existsByNickname(command.nickname) } returns true
            Then("Conflict Exception이 발생한다") {
                shouldThrow<ConflictException> {
                    memberService.create(command)
                }.message shouldBe ALREADY_EXIST_NICKNAME.message
            }
        }
    }

    Given("닉네임 수정 요청 커맨드가 주어진다") {
        val command = NicknameChangeCommand(member.id, "new")
        every { memberRepository.save( any()) } returns member
        every { memberRepository.findByMemberId(any()) } returns member
        every { authenticationUtil.checkPermission(any(), any())} returns Unit

        When("닉네임이 중복되지 않으면") {
            every { memberRepository.existsByNickname(any()) } returns false
            Then("변경된다") {
                val dto = memberService.changeMemberNickname(member.id, command)
                checkEqual(dto, member)
                dto.nickname shouldBe "new"
            }
        }

        When("닉네임이 중복되면") {
            every {memberRepository.existsByNickname(any())} returns true
            Then("Conflict Exception이 발생한다") {
                shouldThrow<ConflictException> {
                    memberService.changeMemberNickname(member.id, command)
                }.message shouldBe ALREADY_EXIST_NICKNAME.message
            }
        }

        When("다른 사람의 닉네임을 변경하려하면") {
            every {memberRepository.existsByNickname(any())} returns false
            every { authenticationUtil.checkPermission(any(), any()) } throws ForbiddenException(
                RESOURCE_OWNERSHIP_VIOLATION
            )
            Then("Forbidden Exception이 발생한다") {
                shouldThrow<ForbiddenException> {
                    memberService.changeMemberNickname(UUID.randomUUID(), command)
                }.message shouldBe RESOURCE_OWNERSHIP_VIOLATION.message
            }
        }
    }

    Given("이메일 수정 커맨드가 주어진다") {
        val command = EmailChangeCommand(memberId = member.id, email = "new")
        member.updateEmail("new")
        every { memberRepository.save( any()) } returns member
        every { memberRepository.findByMemberId(any()) } returns member
        every { authenticationUtil.checkPermission(any(), any())} returns Unit

        When("이메일이 중복되지 않으면") {
            every{memberRepository.existsByEmail(any())} returns false
            Then("변경된다") {
                val dto = memberService.changeMemberEmail(member.id, command)
                checkEqual(dto, member)
                dto.email shouldBe "new"
            }
        }

        When("이메일이 중복되면") {
            every { memberRepository.existsByEmail(any()) } returns true
            Then("Conflict Exception이 발생한다") {
                shouldThrow<ConflictException> {
                    memberService.changeMemberEmail(member.id, command)
                }.message shouldBe ErrorCodes.ALREADY_EXIST_EMAIL.message
            }
        }

        When("다른 사용자의 이메일을 변경하력고 하면") {
            every { memberRepository.existsByEmail(any()) } returns false
            every {authenticationUtil.checkPermission(any(), any())} throws ForbiddenException(
                RESOURCE_OWNERSHIP_VIOLATION
            )
            Then("Forbidden Exception이 발생한다") {
                shouldThrow<ForbiddenException> {
                    memberService.changeMemberEmail(UUID.randomUUID(), command)
                }.message shouldBe RESOURCE_OWNERSHIP_VIOLATION.message
            }
        }
    }

    Given("비밀번호 변경 커맨드가 주어진다") {
        val command = PasswordChangeCommand(
            memberId = member.id,
            currentPassword = memberPassword,
            newPassword = "new-password"
        )

        every { memberRepository.findByMemberId(any()) } returns member

        When("어드민 또는 계정의 사용자가 보낸 요청이 아니라면") {

            every { authenticationUtil.checkPermission(any(), any()) } throws ForbiddenException(RESOURCE_OWNERSHIP_VIOLATION)
            every { authenticationUtil.isAdmin() } returns false

            Then("ForbiddenException이 발생한다") {
               shouldThrow<ForbiddenException>{
                   memberService.changePassword(member.id, command)
               }.message shouldBe RESOURCE_OWNERSHIP_VIOLATION.message
            }
        }

        When("요청 회원이고 현재 비밀번호가 일치하면") {
            member.updatePassword(passwordEncoder.encode(command.currentPassword))
            every { authenticationUtil.checkPermission(any(), any()) } returns Unit
            every { memberRepository.save(any()) } returns member
            every { authenticationUtil.isAdmin() } returns false

            Then("변경된다") {
                val result = memberService.changePassword(member.id, command)
                result.id shouldBe member.id
            }
        }

        When("요청 회원이고 비밀번호가 일치하지 않으면") {
            member.updatePassword(passwordEncoder.encode("notpassword"))
            every { authenticationUtil.checkPermission(any(), any()) } returns Unit
            every { authenticationUtil.isAdmin() } returns false

            Then("UnauthorizedException이 발생한다") {
                shouldThrow<UnauthorizedException> {
                    memberService.changePassword(member.id, command)
                }
            }
        }

        When("어드민 요청이면") {
            every { authenticationUtil.isAdmin() } returns true
            Then("바로 변경된다") {
                val result = memberService.changePassword(member.id, command)
                result.id shouldBe member.id
            }
        }
    }

    Given("삭제하려는 멤버 ID가 주어진다") {

        When("현재 사용자와 일치한다면") {
            every { authenticationUtil.checkPermission(any(), any()) } returns Unit
            Then("삭제된다") {
                memberService.delete(member.id, member.id) shouldBe Unit
            }
        }

        When("현재 사용자와 일치하지 않는다면"){
            every { authenticationUtil.checkPermission(any(), any()) } throws ForbiddenException(
                RESOURCE_OWNERSHIP_VIOLATION
            )
            Then("Forbidden Exception이 발생한다") {
                shouldThrow<ForbiddenException> {
                    memberService.delete(member.id, UUID.randomUUID())
                }.message shouldBe RESOURCE_OWNERSHIP_VIOLATION.message
            }
        }

        When("관리자의 요청이라면") {
            every { authenticationUtil.checkPermission(any(), any())} returns Unit
            Then("삭제된다") {
                memberService.delete(member.id, UUID.randomUUID())
            }
        }
    }

    Given("멤버 목록 조회 요청을 한다") {
        every {memberRepository.count()} returns 1
        every {memberRepository.findAll()} returns listOf(member)

        When("관리자면") {
            every { authenticationUtil.isAdmin() } returns true

            Then("조회된다") {
                val result = memberService.getMembers(null)

                (result is PageList)
                result.count shouldBe  1
                result.data[0] shouldBe MemberDto.fromEntity(member)
            }
        }

        When("일반 사용자면") {
            every { authenticationUtil.isAdmin() } returns false

            Then("Forbidden Exception이 발생한다") {
                shouldThrow<ForbiddenException> {
                    memberService.getMembers(null)
                }
            }
        }
    }

    afterSpec {
        member = createMember(1).first()
        clearAllMocks()
    }
})