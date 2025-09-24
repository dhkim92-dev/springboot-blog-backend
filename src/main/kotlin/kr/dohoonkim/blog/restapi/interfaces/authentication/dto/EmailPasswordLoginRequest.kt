package kr.dohoonkim.blog.restapi.interfaces.authentication.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty

@Schema(description = "이메일/비밀번호 로그인 요청 객체", name = "EmailPasswordLoginRequest")
data class EmailPasswordLoginRequest(
    @Schema(description = "이메일", example = "test@dohoon-kim.kr", required = true)
    @field: NotEmpty
    val email: String,

    @Schema(description = "비밀번호", example = "password1234", required = true)
    @field: NotEmpty
    val password: String
)
