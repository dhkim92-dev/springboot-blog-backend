package kr.dohoonkim.blog.restapi.stash.application.authentication.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ReissueResult(
    val type: String = "Bearer",
    val accessToken: String
)
