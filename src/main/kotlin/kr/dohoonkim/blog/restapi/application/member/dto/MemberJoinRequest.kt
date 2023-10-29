package kr.dohoonkim.blog.restapi.application.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.hibernate.validator.constraints.Length
import org.springframework.web.multipart.MultipartFile

data class MemberJoinRequest(
    @field: Length(message = "닉네임은 최소 4글자, 최대 32글자입니다.", min = 4, max = 32)
    @field: NotBlank(message = "닉네임에는 공백문자가 올 수 없습니다.")
    val nickname: String,

    @field: Email(message = "잘못된 이메일 형식입니다.")
    @field: NotBlank(message = "이메일에는 공백문자가 올 수 없습니다.")
    val email: String,

    @field: Length(min = 8, max = 255, message = "최소 8글자, 최대 255자")
    @field: NotBlank(message = "패스워드에는 공백문자가 들어올 수 없습니다.")
    val password: String
) {
}