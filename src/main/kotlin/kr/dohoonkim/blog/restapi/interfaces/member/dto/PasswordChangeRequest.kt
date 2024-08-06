package kr.dohoonkim.blog.restapi.interfaces.member.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.hibernate.validator.constraints.Length

data class PasswordChangeRequest(
    @field: NotBlank(message="현재 패스워드가 입력되어야 합니다.")
    val currentPassword: String,

    @field: NotBlank(message="변경할 패스워드가 입력되어야 합니다.")
    @field: Length(min = 8, max = 255, message = "패스워드의는 최소 8글자 최대 255글자 입니다.")
    val newPassword: String
)
