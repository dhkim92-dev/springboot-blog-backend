package kr.dohoonkim.blog.restapi.interfaces.authentication.dto

import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginResult

@Schema(description = "로그인 응답 객체", name = "LoginResponse")
data class LoginResponse(
    @Schema(description = "토큰 타입", example = "Bearer", required = true)
    val type: String = "Bearer",

    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    val refreshToken: String,

    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", required = true)
    val accessToken: String
) {
    
    companion object {

        fun from(result: LoginResult): LoginResponse {
            return LoginResponse(
                type = result.type,
                refreshToken = result.refreshToken,
                accessToken = result.accessToken
            )
        }
    }
}