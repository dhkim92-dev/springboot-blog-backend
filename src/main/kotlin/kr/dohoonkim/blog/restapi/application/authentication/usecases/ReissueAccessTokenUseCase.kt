package kr.dohoonkim.blog.restapi.application.authentication.usecases

import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginResult

interface ReissueAccessTokenUseCase {

    fun reissue(response: HttpServletResponse, refreshToken: String): LoginResult
}