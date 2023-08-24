package kr.dohoonkim.blog.restapi.application.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

data class MemberCreateDto(
    val nickname : String,
    val email : String,
    val password : String
) {
}