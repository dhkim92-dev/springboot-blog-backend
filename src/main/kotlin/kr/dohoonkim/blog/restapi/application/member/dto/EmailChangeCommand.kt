package kr.dohoonkim.blog.restapi.application.member.dto

import jakarta.validation.constraints.Email
import java.util.*

data class EmailChangeCommand(
    val memberId: UUID,
    @field: Email(message="email should be email format")
    val email: String
)
