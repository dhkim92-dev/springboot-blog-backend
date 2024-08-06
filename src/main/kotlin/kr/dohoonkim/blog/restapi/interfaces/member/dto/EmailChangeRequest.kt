package kr.dohoonkim.blog.restapi.interfaces.member.dto

import jakarta.validation.constraints.Email

data class EmailChangeRequest(
    @field: Email
    val email: String
)
