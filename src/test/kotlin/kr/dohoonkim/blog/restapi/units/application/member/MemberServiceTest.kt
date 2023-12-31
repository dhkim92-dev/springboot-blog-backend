package kr.dohoonkim.blog.restapi.units.application.member

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kr.dohoonkim.blog.restapi.application.member.MemberService
import kr.dohoonkim.blog.restapi.application.member.dto.*
import kr.dohoonkim.blog.restapi.common.error.ErrorCode.RESOURCE_OWNERSHIP_VIOLATION
import kr.dohoonkim.blog.restapi.common.error.exceptions.ConflictException
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import kr.dohoonkim.blog.restapi.common.utility.AuthenticationUtil
import kr.dohoonkim.blog.restapi.domain.member.Role
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.support.entity.createMember
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class MemberServiceTest : BehaviorSpec({

    val memberRepository = mockk<MemberRepository>()
    val authenticationUtils = mockk<AuthenticationUtil>()
    val passwordEncoder = mockk<BCryptPasswordEncoder>()
    val memberService: MemberService = MemberService(memberRepository, passwordEncoder, authenticationUtils)

    val admin = createMember()
    val user = createMember()
    val other = createMember()

    admin.role = Role.ADMIN
    user.role = Role.MEMBER
    other.role = Role.MEMBER

    Given("회원가입을 하려한다.") {
        every { memberRepository.save(any()) } returns user
        val dto = MemberCreateDto(user.nickname, user.email, user.password)

        When("이메일로 가입된 이력이 없고, 닉네임이 중복되지 않으면") {
            every { memberRepository.existsByEmail(any()) } returns false
            every { memberRepository.existsByNickname(any()) } returns false
            every { passwordEncoder.encode(any()) } returns ""

            Then("회원 가입이 된다") {
                val memberDto = memberService.create(dto)

                memberDto shouldBe MemberDto.fromEntity(user)
            }
        }

        When("이메일이 중복되면") {
            every { memberRepository.existsByEmail(any()) } returns true
            every { memberRepository.existsByNickname(any()) } returns false

            Then("회원가입이 되지 않는다.") {
                shouldThrow<ConflictException> {
                    memberService.create(dto)
                }
            }
        }

        When("닉네임이 중복되면") {
            every { memberRepository.existsByEmail(any()) } returns false
            every { memberRepository.existsByNickname(any()) } returns true

            Then("회원가입이 되지 않는다.") {
                shouldThrow<ConflictException> {
                    memberService.create(dto)
                }
            }
        }
    }

    Given("회원을 삭제한다.") {
        every { memberRepository.deleteById(any()) } returns Unit

        When("자기 자신을 삭제하려고 하면") {
            every { authenticationUtils.isAdmin() } returns false
            every { authenticationUtils.checkPermission(any(), any()) } returns Unit

            Then("회원 탈퇴된다.") {
                memberService.delete(user.id, user.id) shouldBe Unit
            }
        }

        When("관리자가 삭제를 시도하면") {
            every { authenticationUtils.checkPermission(admin.id, user.id) } returns Unit

            Then("회원 탈퇴된다.") {
                memberService.delete(admin.id, user.id) shouldBe Unit
            }
        }

        When("관리자가 아닌 사용자가 다른 사용자를 삭제하려하면") {
            every { authenticationUtils.checkPermission(other.id, user.id) } throws ForbiddenException(
                RESOURCE_OWNERSHIP_VIOLATION
            )

            Then("에러가 발생한다.") {
                shouldThrow<ForbiddenException> {
                    memberService.delete(other.id, user.id)
                }
            }
        }
    }

    Given("패스워드를 변경한다.") {
        val dto = MemberPasswordChangeDto(
            user.id,
            user.password,
            "newPassword"
        )
        every { passwordEncoder.encode(any()) } returns ""

        When("자신의 패스워드를 변경하면") {
            every { memberRepository.save(any()) } returns user
            every { memberRepository.findByMemberId(any()) } returns user
            every { authenticationUtils.checkPermission(user.id, dto.memberId) } returns Unit

            Then("현재 비밀번호가 일치하면 변경에 성공한다.") {
                every { passwordEncoder.matches(any(), any()) } returns true

                val memberDto = memberService.changePassword(user.id, dto)

                memberDto.id shouldBe user.id
            }

            Then("현재 비밀번호가 일치하지 않으면 실패한다.") {
                every { passwordEncoder.matches(any(), any()) } returns false

                shouldThrow<UnauthorizedException> {
                    memberService.changePassword(user.id, dto)
                }
            }
        }

        When("다른 사람의 패스워드 변경을 시도하면") {
            every { passwordEncoder.matches(any(), any()) } returns true
            every { authenticationUtils.checkPermission(other.id, dto.memberId) } throws ForbiddenException(
                RESOURCE_OWNERSHIP_VIOLATION
            )

            Then("에러가 발생한다.") {
                shouldThrow<ForbiddenException> {
                    memberService.changePassword(other.id, dto)
                }
            }
        }
    }

    Given("이메일을 변경한다.") {
        val emailChangeDto = MemberEmailChangeDto(user.id, "newEmail@gmail.com")
        every { memberRepository.save(any()) } returns user

        When("중복되지 않는 이메일로 자신의 이메일을 변경하면") {
            every { memberRepository.existsByEmail(any()) } returns false
            every { authenticationUtils.checkPermission(user.id, emailChangeDto.memberId) } returns Unit

            Then("이메일이 변경된다.") {
                val ret = memberService.changeMemberEmail(user.id, emailChangeDto)
                ret.email shouldBe emailChangeDto.email
            }
        }

        When("이메일이 중복될 경우") {
            every { memberRepository.existsByEmail(any()) } returns true
            every { authenticationUtils.checkPermission(any(), any()) } returns Unit

            Then("에러가 발생한다.") {
                shouldThrow<ConflictException> {
                    memberService.changeMemberEmail(user.id, emailChangeDto)
                }
            }
        }

        When("다른 사람의 이메일을 변경하면") {
            every { memberRepository.existsByEmail(any()) } returns false
            every { authenticationUtils.checkPermission(user.id, any()) } throws ForbiddenException(
                RESOURCE_OWNERSHIP_VIOLATION
            )

            Then("에러가 발생한다.") {
                val otherMemberEmailChangeDto = MemberEmailChangeDto(
                    other.id,
                    emailChangeDto.email
                )
                print("other id : ${other.id} dto memberId : ${otherMemberEmailChangeDto.memberId}")

                shouldThrow<ForbiddenException> {
                    memberService.changeMemberEmail(user.id, otherMemberEmailChangeDto)
                }
            }
        }
    }

    Given("닉네임을 변경한다.") {
        val dto = MemberNicknameChangeDto(
            memberId = user.id,
            nickname = "newNickname"
        )
        every { authenticationUtils.extractAuthenticationMember() } returns user
        every { memberRepository.save(any()) } returns user

        When("중복되지 않는 닉네임으로 자기 자신의 닉네임을 변경하면") {
            every { memberRepository.existsByNickname(any()) } returns false
            every { authenticationUtils.checkPermission(user.id, user.id) } returns Unit

            Then("이메일이 변경된다.") {
                val ret = memberService.changeMemberNickname(user.id, dto)

                ret.nickname shouldBe dto.nickname
            }
        }

        When("닉네임이 중복될 경우") {
            every { memberRepository.existsByNickname(any()) } returns true
            every { authenticationUtils.checkPermission(any(), any()) } returns Unit

            Then("에러가 발생한다.") {
                shouldThrow<ConflictException> {
                    memberService.changeMemberNickname(user.id, dto)
                }
            }
        }

        When("다른 사람의 닉네임을 변경하면") {
            every { memberRepository.existsByNickname(any()) } returns false
            every { authenticationUtils.isAdmin() } returns false
            every { authenticationUtils.checkPermission(any(), any()) } throws ForbiddenException(
                RESOURCE_OWNERSHIP_VIOLATION
            )

            Then("에러가 발생한다.") {
                val otherMemberNicknameChangeDto = MemberNicknameChangeDto(
                    memberId = other.id,
                    nickname = "newNickname"
                )
                print("other id : ${user.id} dto memberId : ${otherMemberNicknameChangeDto.memberId}")

                shouldThrow<ForbiddenException> {
                    memberService.changeMemberNickname(user.id, otherMemberNicknameChangeDto)
                }
            }
        }
    }

    afterSpec { clearAllMocks() }
})