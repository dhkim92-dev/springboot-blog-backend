package kr.dohoonkim.blog.restapi.application.authentication.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ReissueTokenRequest(
        @JsonProperty("refresh_token")
        val refreshToken : String
)
