package kr.dohoonkim.blog.restapi.stash.interfaces.authentication.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.application.authentication.dto.ReissueResult

/**
 * JWT Access token 재발급 요청 응답 객체
 * @property type 토큰 타입
 * @property accessToken 액세스 토큰
 */
@Schema(description = "JWT Access token 재발급 요청 응답 객체, 새 Access Token은 Authorization header에 포함됨")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class ReissueResponse(
    @Schema(description = "Token type", example = "Bearer")
    val type: String,
    val accessToken: String
){

    companion object {
        fun valueOf(result: ReissueResult) = ReissueResponse(result.type, result.accessToken)
    }
}