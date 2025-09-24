package kr.dohoonkim.blog.restapi.application.authentication.impl

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import jakarta.servlet.http.HttpServletResponse
import jakarta.transaction.Transactional
import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginResult
import kr.dohoonkim.blog.restapi.application.authentication.jwt.JwtService
import kr.dohoonkim.blog.restapi.application.authentication.usecases.ReissueAccessTokenUseCase
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import kr.dohoonkim.blog.restapi.common.utility.CookieUtils
import kr.dohoonkim.blog.restapi.domain.member.RefreshToken
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID

@Component
class ReissueAccessTokenUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val jwtService: JwtService
): ReissueAccessTokenUseCase {

    private val logger = LoggerFactory.getLogger(javaClass)

    companion object {

        private const val REFRESH_TOKEN_RENEWAL_THRESHOLD_DAYS = 1L
    }

    @Transactional
    override fun reissue(
//        response: HttpServletResponse,
        refreshToken: String
    ): LoginResult {
        val decodedJWT = try {
            jwtService.validateRefreshToken(refreshToken)
        } catch (e: TokenExpiredException) {
//            CookieUtils.delCookie(response, "refresh-token")
            logger.error("Invalid refresh token: ${e.message}")
            throw UnauthorizedException(ErrorCodes.EXPIRED_REFRESH_TOKEN_EXCEPTION)
        } catch (e: JWTVerificationException) {
//            CookieUtils.delCookie(response, "refresh-token")
            logger.error("Invalid refresh token: ${e.message}")
            throw UnauthorizedException(ErrorCodes.INVALID_JWT_EXCEPTION)
        }

        val memberId = UUID.fromString(decodedJWT.subject)
        val member = memberRepository.findByIdOrNull(memberId)
            ?: throw UnauthorizedException(ErrorCodes.MEMBER_NOT_FOUND)
        val accessToken = jwtService.generateAccessToken(member)

        // 토큰 만료 시간이 하루 이하로 남았다면 새로운 리프레시 토큰을 발급하고,
        // 멤버 엔티티에 저장된 기존 토큰을 삭제 한 뒤, 새로운 토큰을 저장한다.
        // 모든 연산은 Instant를 기준으로 작성한다.
        val refreshTokenExpiry = decodedJWT.expiresAt.toInstant()
        val now = Instant.now()
        val daysUntilExpiry = ChronoUnit.DAYS.between(now, refreshTokenExpiry)

        if ( daysUntilExpiry <= REFRESH_TOKEN_RENEWAL_THRESHOLD_DAYS ) {
            val newRefreshToken = jwtService.generateRefreshToken(member)
            val decoded = jwtService.validateRefreshToken(newRefreshToken)
            member.logout(refreshToken)
            member.addRefreshToken(newRefreshToken, decoded.expiresAt.toInstant())
//            CookieUtils.setCookie(response, "refresh-token", newRefreshToken, maxAge = 60 * 60 * 24 * 14)
            return LoginResult(accessToken = accessToken, refreshToken = newRefreshToken)
        }

        return LoginResult(accessToken = accessToken, refreshToken = refreshToken)
    }
}