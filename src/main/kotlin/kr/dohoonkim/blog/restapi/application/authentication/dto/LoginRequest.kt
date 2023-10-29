package kr.dohoonkim.blog.restapi.application.authentication.dto

import jakarta.validation.constraints.Email
import org.hibernate.validator.constraints.Length

data class LoginRequest(
    @field : Email
    var email: String,
    @field : Length(min = 8, max = 64)
    var password: String
) {
}
