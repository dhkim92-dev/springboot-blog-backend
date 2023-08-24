package kr.dohoonkim.blog.restapi.application.member.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty
import org.hibernate.validator.constraints.Length

data class NicknameChangeRequest(
        @field: NotEmpty
        @field: Length(min=4, max=32)
        val nickname : String) {

}
