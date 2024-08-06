package kr.dohoonkim.blog.restapi.interfaces.authentication.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.authentication.dto.ReissueResult

/**
 * JWT Access token 재발급 요청 응답 객체
 * @property type 토큰 타입
 * @property accessToken 액세스 토큰
 */
@Schema(description = "JWT Access token 재발급 요청 응답 객체")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class ReissueResponse(
    @Schema(description = "Token type", example = "Bearer")
    val type: String,
    @Schema(description = "Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5cv")
    val accessToken: String
){

    companion object {
        fun valueOf(result: ReissueResult) = ReissueResponse(result.type, result.accessToken)
    }
}