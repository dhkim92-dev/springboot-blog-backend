package kr.dohoonkim.blog.restapi.interfaces.authentication.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.NotBlank

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ReissueTokenRequest(
    @field: NotBlank(message = "Refresh token이 필요합니다")
    val refreshToken: String
)
