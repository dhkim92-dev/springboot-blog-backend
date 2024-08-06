package kr.dohoonkim.blog.restapi.application.authentication.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ReissueResult(
    val type: String = "Bearer",
    val accessToken: String
)
