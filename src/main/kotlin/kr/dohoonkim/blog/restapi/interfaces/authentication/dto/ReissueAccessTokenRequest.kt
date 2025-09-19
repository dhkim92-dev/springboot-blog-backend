package kr.dohoonkim.blog.restapi.interfaces.authentication.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "액세스 토큰 재발급 요청 객체")
class ReissueAccessTokenRequest(
    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjg4NzI5MjAwLCJleHAiOjE3MTkzMjUyMDB9.4pO7Y3lKXoOZCk1vQYH2jv0nV8FQxX5Fq7mXH3U1hU" )
    @field: NotBlank(message = "리프레시 토큰은 필수입니다.")
    val refreshToken: String
) {

}