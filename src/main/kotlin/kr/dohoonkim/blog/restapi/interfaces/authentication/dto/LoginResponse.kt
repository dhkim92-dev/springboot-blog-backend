package kr.dohoonkim.blog.restapi.interfaces.authentication.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginResult


/**
 * Login 성공 시 반환 응답 객체
 * @property type 인증 토큰 타입
 * @property refreshToken JWT Refresh token
 * @property accessToken JWT Access token
 */
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Schema(description = "로그인 결과 응답 객체")
class LoginResponse(
    @Schema(description = "Token Type", example = "Bearer")
    val type: String = "Bearer",
    @Schema(description = "Refresh token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    val refreshToken: String,
    @Schema(description = "Access token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
    val accessToken: String
){
    companion object {
        fun valueOf(result: LoginResult): LoginResponse {
            return LoginResponse(
                type = result.type,
                refreshToken =  result.refreshToken,
                accessToken = result.accessToken
            )
        }
    }
}