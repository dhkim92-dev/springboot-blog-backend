package kr.dohoonkim.blog.restapi.interfaces

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import kr.dohoonkim.blog.restapi.application.member.MemberService
import kr.dohoonkim.blog.restapi.application.member.dto.*
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.ApiResult.*
import kr.dohoonkim.blog.restapi.common.response.ApiResult.Companion.Ok
import kr.dohoonkim.blog.restapi.common.response.ResultCode.*
import kr.dohoonkim.blog.restapi.security.annotations.MemberId
import org.hibernate.validator.constraints.Length
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
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
    fun getMembers(pageable: Pageable?) = Ok(GET_MEMBER_LIST_SUCCESS, this.memberService.getMembers(pageable))

    @PostMapping("v1/members")
    fun createMember(@RequestBody @Valid request: MemberJoinRequest): ResponseEntity<ApiResult<MemberDto>> {
        log.info("Member join request : $request")
        val dto = MemberCreateDto(
            nickname = request.nickname,
            email = request.email,
            password = request.password
        )
        return Ok(CREATE_MEMBER_SUCCESS, this.memberService.create(dto));
    }

    @GetMapping("v1/members/email/exists")
    fun checkEmail(@RequestParam("value") @Valid @Email @NotBlank @NotEmpty email: String) =
        Ok(AVAILABLE_EMAIL, this.memberService.checkEmailAvailable(email))

    @GetMapping("v1/members/nickname/exists")
    fun checkNickname(@RequestParam("value") @Valid @Length(min = 4, max = 30) nickname: String) =
        Ok(AVAILABLE_NICKNAME, this.memberService.checkNicknameAvailable(nickname))

    @PatchMapping("v1/members/{resourceId}/nickname")
    fun changeNickname(
        @PathVariable resourceId: UUID,
        @RequestBody @Valid request: NicknameChangeRequest,
        @MemberId memberId: UUID
    ): ResponseEntity<ApiResult<MemberDto>> {
        val dto = MemberNicknameChangeDto(
            memberId = resourceId,
            nickname = request.nickname
        )

        return Ok(CHANGE_NICKNAME_SUCCESS, this.memberService.changeMemberNickname(memberId, dto))
    }

    @PatchMapping("v1/members/{resourceId}/email")
    fun changeEmail(
        @PathVariable resourceId: UUID,
        @RequestBody @Valid request: EmailChangeRequest,
        @MemberId memberId : UUID
    ): ResponseEntity<ApiResult<MemberDto>> {
        val dto = MemberEmailChangeDto(resourceId, request.email)
        return Ok(CHANGE_EMAIL_SUCCESS, this.memberService.changeMemberEmail(memberId, dto))
    }

    @PatchMapping("v1/members/{resourceId}/password")
    fun changePassword(
        @PathVariable resourceId: UUID,
        @RequestBody @Valid request: PasswordChangeRequest,
        @MemberId memberId: UUID
    ): ResponseEntity<ApiResult<MemberDto>> {
        val dto = MemberPasswordChangeDto(
            memberId = resourceId,
            currentPassword = request.currentPassword,
            newPassword = request.newPassword
        )

        return Ok(CHANGE_PASSWORD_SUCCESS, this.memberService.changePassword(memberId, dto))
    }

    @DeleteMapping("v1/members/{resourceId}")
    fun withdrawal(@PathVariable resourceId: UUID, @MemberId memberId: UUID): ResponseEntity<ApiResult<Unit>> =
        Ok(DELETE_MEMBER_SUCCESS, this.memberService.delete(memberId, resourceId));

}