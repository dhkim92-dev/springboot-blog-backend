//package kr.dohoonkim.blog.restapi.stash.application.authentication

import kr.dohoonkim.blog.restapi.application.authentication.dto.*
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.MEMBER_NOT_FOUND
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.NOT_VERIFIED_EMAIL
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * Authentication Service
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 * @property jwtService
 * @property memberRepository
 * @property userDetailService
 * @property passwordEncoder
 */
@Transactional(readOnly = true)
@Service
class AuthenticationService(
    private val jwtService: JwtService,
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun login(email: String, password: String): LoginResult {

        val member = memberRepository.findByEmail(email)
            ?: throw NotFoundException(MEMBER_NOT_FOUND)

        if (!passwordEncoder.matches(password, member.password)) {
            throw BadCredentialsException("email/password mismatched.");
        }

        if (!member.isActivated) {
            throw UnauthorizedException(NOT_VERIFIED_EMAIL)
        }

        return LoginResult(
            refreshToken = jwtService.createRefreshToken(member),
            refreshTokenExpiry = (jwtService.config.refreshExpiry/1000).toInt(),
            accessToken = jwtService.createAccessToken(member)
        )
    }

    @Transactional
    fun reIssueAccessToken(refreshToken: String): ReissueResult {
        val jwt = jwtService.verifyRefreshToken(refreshToken)
        val memberId = UUID.fromString(jwt.subject)
        val member: Member = memberRepository.findByMemberId(memberId)

        return ReissueResult(
            accessToken =  jwtService.createAccessToken(member)
        )
    }
}