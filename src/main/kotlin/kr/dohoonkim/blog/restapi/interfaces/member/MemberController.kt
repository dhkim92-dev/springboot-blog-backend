package kr.dohoonkim.blog.restapi.interfaces.member

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.dohoonkim.blog.restapi.application.member.MemberServiceFacade
import kr.dohoonkim.blog.restapi.application.member.dto.*
import kr.dohoonkim.blog.restapi.common.response.ResultCode.*
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import kr.dohoonkim.blog.restapi.interfaces.member.dto.MemberJoinRequest
import kr.dohoonkim.blog.restapi.interfaces.member.dto.ChangeMemberInfoRequest
import kr.dohoonkim.blog.restapi.interfaces.member.dto.PasswordChangeRequest
import kr.dohoonkim.blog.restapi.common.annotations.MemberId
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.interfaces.member.dto.MemberQueryResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("api/")
@Tag(name = "Member API", description = "사용자 가입, 수정, 탈퇴 기능 제공")
class MemberController(private val memberService: MemberServiceFacade) {

    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping("v1/members")
    @ApplicationCode(CREATE_MEMBER_SUCCESS)
    fun createMember(@RequestBody @Valid request: MemberJoinRequest): MemberDto {
        val dto = CreateMemberCommand(
            nickname = request.nickname,
            email = request.email,
            password = request.password
        )
        return memberService.createMember(dto)
    }

    @PatchMapping("v1/members/{resourceId}/password")
    @ApplicationCode(CHANGE_PASSWORD_SUCCESS)
    fun changePassword(
        @MemberId memberId: UUID,
        @PathVariable resourceId: UUID,
        @RequestBody @Valid request: PasswordChangeRequest
    ) {
        val command = request.toCommand(resourceId)
        memberService.updatePassword(memberId, command)
    }

    @PutMapping("v1/members/{resourceId}")
    @ApplicationCode(ResultCode.CHANGE_NICKNAME_SUCCESS)
    fun changeMemberInfo(
        @MemberId memberId: UUID,
        @PathVariable resourceId: UUID,
        @RequestBody @Valid request: ChangeMemberInfoRequest
    ) {
        val command = request.toCommand(resourceId)
        memberService.updateMember(memberId, command)
    }

    @DeleteMapping("v1/members/{resourceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMember(
        @MemberId memberId: UUID,
        @PathVariable resourceId: UUID
    ) {
        memberService.deleteMember(memberId, resourceId)
    }

    @GetMapping("v1/members/{resourceId}")
    @ApplicationCode(ResultCode.GET_MEMBER_INFO_SUCCESS)
    fun getMember(
        @MemberId memberId: UUID,
        @PathVariable resourceId: UUID
    ): MemberQueryResponse {
        return MemberQueryResponse.from(memberService.getMember(memberId, resourceId))
    }
}