package kr.dohoonkim.blog.restapi.application.authentication.impl

import jakarta.transaction.Transactional
import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginResult
import kr.dohoonkim.blog.restapi.application.authentication.jwt.JwtService
import kr.dohoonkim.blog.restapi.application.authentication.usecases.LoginByEmailPasswordUseCase
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.domain.member.RefreshToken
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class LoginByEmailPasswordUseCaseImpl(
    private val jwtService: JwtService,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
): LoginByEmailPasswordUseCase {

    @Transactional
    override fun login(email: String, password: String): LoginResult {
        val member = memberRepository.findByEmail(email)
            ?: throw NotFoundException(ErrorCodes.MEMBER_NOT_FOUND)

        if ( member.isBlocked ) {
            throw ForbiddenException(ErrorCodes.BLOCKED_MEMBER)
        }

        if( !passwordEncoder.matches(password, member.password) ) {
            throw ForbiddenException(ErrorCodes.AUTHENTICATION_FAIL)
        }

        val accessToken = jwtService.generateAccessToken(member)
        val refreshToken = jwtService.generateRefreshToken(member)
        val decodedJWT = jwtService.validateRefreshToken(refreshToken)

        member.addRefreshToken(refreshToken,decodedJWT.expiresAt.toInstant())
        memberRepository.save(member)

        return LoginResult(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}