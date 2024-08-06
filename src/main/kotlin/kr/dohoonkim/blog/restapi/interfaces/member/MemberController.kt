package kr.dohoonkim.blog.restapi.interfaces.member

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import kr.dohoonkim.blog.restapi.application.member.MemberService
import kr.dohoonkim.blog.restapi.application.member.dto.*
import kr.dohoonkim.blog.restapi.common.response.ApiResult.*
import kr.dohoonkim.blog.restapi.common.response.ResultCode.*
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import kr.dohoonkim.blog.restapi.interfaces.member.dto.EmailChangeRequest
import kr.dohoonkim.blog.restapi.interfaces.member.dto.MemberJoinRequest
import kr.dohoonkim.blog.restapi.interfaces.member.dto.NicknameChangeRequest
import kr.dohoonkim.blog.restapi.interfaces.member.dto.PasswordChangeRequest
import kr.dohoonkim.blog.restapi.security.annotations.MemberId
import org.hibernate.validator.constraints.Length
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("api/")
@Tag(name = "Member API", description = "사용자 가입, 수정, 탈퇴 기능 제공")
class MemberController(private val memberService: MemberService) {

    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("v1/members")
    @ApplicationCode(GET_MEMBER_LIST_SUCCESS)
    fun getMembers(pageable: Pageable?) = memberService.getMembers(pageable)

    @PostMapping("v1/members")
    @ApplicationCode(CREATE_MEMBER_SUCCESS)
    fun createMember(@RequestBody @Valid request: MemberJoinRequest): MemberDto {
        val dto = MemberCreateCommand(
            nickname = request.nickname,
            email = request.email,
            password = request.password
        )
        return memberService.create(dto)
    }

    @GetMapping("v1/members/email/exists")
    @ApplicationCode(AVAILABLE_EMAIL)
    fun checkEmail(@RequestParam("value") @Valid @Email @NotBlank @NotEmpty email: String)
    = memberService.checkEmailAvailable(email)

    @GetMapping("v1/members/nickname/exists")
    @ApplicationCode(AVAILABLE_NICKNAME)
    fun checkNickname(@RequestParam("value") @Valid @Length(min = 4, max = 30) nickname: String) =
        memberService.checkNicknameAvailable(nickname)

    @PatchMapping("v1/members/{resourceId}/nickname")
    @ApplicationCode(CHANGE_NICKNAME_SUCCESS)
    fun changeNickname(
        @RequestBody @Valid request: NicknameChangeRequest,
        @PathVariable resourceId: UUID,
        @MemberId memberId: UUID
    ): MemberDto{
        val dto = NicknameChangeCommand(
            memberId = resourceId,
            nickname = request.nickname
        )

        return memberService.changeMemberNickname(memberId, dto)
    }

    @PatchMapping("v1/members/{resourceId}/email")
    @ApplicationCode(CHANGE_EMAIL_SUCCESS)
    fun changeEmail(
        @MemberId memberId : UUID,
        @PathVariable resourceId: UUID,
        @RequestBody @Valid request: EmailChangeRequest
    ): MemberDto {
        val dto = EmailChangeCommand(resourceId, request.email)
        return memberService.changeMemberEmail(memberId, dto)
    }

    @PatchMapping("v1/members/{resourceId}/password")
    @ApplicationCode(CHANGE_PASSWORD_SUCCESS)
    fun changePassword(
        @MemberId memberId: UUID,
        @PathVariable resourceId: UUID,
        @RequestBody @Valid request: PasswordChangeRequest
    ): MemberDto {
        val dto = PasswordChangeCommand(
            memberId = resourceId,
            currentPassword = request.currentPassword,
            newPassword = request.newPassword
        )

        return memberService.changePassword(memberId, dto)
    }

    @DeleteMapping("v1/members/{resourceId}")
    @ApplicationCode(DELETE_MEMBER_SUCCESS)
    fun withdrawal(@PathVariable resourceId: UUID, @MemberId memberId: UUID)
    = memberService.delete(memberId, resourceId)
}