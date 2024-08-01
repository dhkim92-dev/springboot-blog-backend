package kr.dohoonkim.blog.restapi.interfaces.authentication.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

/**
 * Email/Password 로그인 요청 객체
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 * @property email Email
 * @property password 비밀번호
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Schema(description = "Email/Password 로그인 요청 객체")
class LoginRequest(
    @field : Email(message = "이메일 형식이 제출되어야 합니다")
    @Schema(description = "Email/Password 로그인 요청 객체", example = "example@dohoon-kim.kr", required =  true)
    val email: String,
    @field : Length(min = 8, max = 64)
    @field: NotBlank(message = "패스워드가 제출되어야 합니다")
    @Schema(description = "Email/Password 로그인 요청 객체", example = "password1234", required = true)
    val password: String
) {

}
