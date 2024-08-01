package kr.dohoonkim.blog.restapi.application.member.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import org.hibernate.validator.constraints.Length

@Schema(description = "닉네임 변경 요청 객체")
data class NicknameChangeRequest(
    @field: NotEmpty
    @field: Length(min = 4, max = 32)
    @Schema(description = "사용자 닉네임", example = "my-new-nickname")
    val nickname: String
) {

}
