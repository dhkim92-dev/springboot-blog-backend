package kr.dohoonkim.blog.restapi.application.authentication.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "성공 시 반환 리프레시/억세스 토큰")
data class LoginResult(
    val type: String = "Bearer",
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("access_token")
    val accessToken: String
)
