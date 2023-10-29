package kr.dohoonkim.blog.restapi.application.authentication.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ReissueResult(
    val type: String = "Bearer",
    @JsonProperty("access_token")
    val accessToken: String
)
