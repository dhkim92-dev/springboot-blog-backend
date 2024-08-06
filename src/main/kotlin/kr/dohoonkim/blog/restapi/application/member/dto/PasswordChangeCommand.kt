package kr.dohoonkim.blog.restapi.application.member.dto

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import java.util.UUID

data class PasswordChangeCommand(
    val memberId: UUID,
    val currentPassword: String,
    @field: NotBlank(message = "invalid password")
    @field: Length(min=8, max=64, message = "password required at least 8 at most 64 characters")
    val newPassword: String
)
