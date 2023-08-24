package kr.dohoonkim.blog.restapi.application.member.dto

import jakarta.validation.constraints.NotEmpty
import org.hibernate.validator.constraints.Length

data class PasswordChangeRequest(
        @field: NotEmpty
        val currentPassword : String,

        @field: NotEmpty
        @field: Length(min = 8, max=255)
        val newPassword : String)
