package kr.dohoonkim.blog.restapi.application.authentication.usecases

interface RevokeRefreshTokenUseCase {

    fun revoke(refreshToken: String)
}