package kr.dohoonkim.blog.restapi.stash.interfaces.authentication.dto

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
@Schema(description = "로그인 결과 응답 객체, Refresh Token은 쿠키에, Access Token은 Authorization Header에 포함됨")
class LoginResponse(
    @Schema(description = "Token Type", example = "Bearer")
    val type: String = "Bearer",
    val accessToken: String = ""
){
    companion object {
        fun valueOf(result: LoginResult): LoginResponse {
            return LoginResponse(
                type = result.type,
                accessToken = result.accessToken
            )
        }
    }
}