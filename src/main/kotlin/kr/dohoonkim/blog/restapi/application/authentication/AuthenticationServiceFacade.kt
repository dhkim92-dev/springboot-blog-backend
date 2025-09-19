package kr.dohoonkim.blog.restapi.application.authentication

import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginResult
import kr.dohoonkim.blog.restapi.application.authentication.usecases.LoginByEmailPasswordUseCase
import kr.dohoonkim.blog.restapi.application.authentication.usecases.ReissueAccessTokenUseCase
import kr.dohoonkim.blog.restapi.application.authentication.usecases.RevokeRefreshTokenUseCase
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceFacade(
    private val emailPasswordUseCase: LoginByEmailPasswordUseCase,
    private val reissueAccessTokenUseCase: ReissueAccessTokenUseCase,
    private val revokeRefreshTokenUseCase: RevokeRefreshTokenUseCase
) {

    fun loginByEmailPassword(email: String, password: String): LoginResult {
        return emailPasswordUseCase.login(email, password)
    }

    fun reissueAccessToken(response: HttpServletResponse, refreshToken: String): LoginResult {
        return reissueAccessTokenUseCase.reissue(response,refreshToken)
    }

    fun logout(refreshToken: String) {
        return revokeRefreshTokenUseCase.revoke(refreshToken)
    }
}