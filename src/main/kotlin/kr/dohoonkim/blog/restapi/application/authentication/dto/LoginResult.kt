package kr.dohoonkim.blog.restapi.application.authentication.dto

class LoginResult(
    val type: String = "Bearer",
    val refreshToken: String,
    val accessToken: String
) {
}