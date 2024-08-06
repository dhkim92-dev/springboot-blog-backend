package kr.dohoonkim.blog.restapi.application.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class MemberCreateCommand(
    @field: NotBlank(message = "invalid nickname")
    val nickname: String,
    @field: Email(message = "invalid email")
    val email: String,
    @field: NotBlank(message = "invalid password")
    @field: Length(min=8, max=64, message = "password required at least 8 at most 64 characters")
    val password: String
) {
}