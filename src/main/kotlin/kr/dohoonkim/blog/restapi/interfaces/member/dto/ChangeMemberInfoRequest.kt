package kr.dohoonkim.blog.restapi.interfaces.member.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import kr.dohoonkim.blog.restapi.application.member.dto.UpdateMemberCommand
import org.hibernate.validator.constraints.Length
import java.util.UUID

@Schema(description = "회원 정보 수정 요청 객체")
data class ChangeMemberInfoRequest(
    @field: NotBlank(message = "닉네임이 입력되어야 합니다.")
    @field: Length(min = 4, max = 32, message = "닉네임은 최소 4글자 최대 32글자 입니다.")
    @Schema(description = "사용자 닉네임", example = "my-new-nickname")
    val nickname: String,
) {

    fun toCommand(resourceId: UUID): UpdateMemberCommand {
        return UpdateMemberCommand(
            targetId = resourceId,
            nickname = this.nickname
        )
    }
}
