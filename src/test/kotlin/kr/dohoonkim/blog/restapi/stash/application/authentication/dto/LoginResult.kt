package kr.dohoonkim.blog.restapi.stash.application.authentication.dto

import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema

data class LoginResult(
    val type: String = "Bearer",
    val refreshToken: String,
    val accessToken: String
)
