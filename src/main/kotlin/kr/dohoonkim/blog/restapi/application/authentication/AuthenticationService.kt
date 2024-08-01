package kr.dohoonkim.blog.restapi.application.authentication

import kr.dohoonkim.blog.restapi.application.authentication.dto.*
import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
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
    private val userDetailService: CustomUserDetailService,
    private val passwordEncoder: PasswordEncoder
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun login(email: String, password: String): LoginResult {
        val user = userDetailService.loadUserByUsername(email) as CustomUserDetails

        if (!user.isEnabled) {
            throw UnauthorizedException(ErrorCode.NOT_VERIFIED_EMAIL)
        }

        if (!passwordEncoder.matches(password, user.password)) {
            throw BadCredentialsException("email/password mismatched.");
        }

        val claims = JwtClaims.fromCustomUserDetails(user)

        return LoginResult(
            refreshToken = jwtService.createRefreshToken(claims),
            accessToken = jwtService.createAccessToken(claims)
        )
    }

    @Transactional
    fun reIssueAccessToken(refreshToken: String): ReissueResult {
        val jwt = jwtService.verifyRefreshToken(refreshToken)
        val memberId = UUID.fromString(jwt.subject)
        val member: Member = memberRepository.findByMemberId(memberId)
        val userDetails = userDetailService.loadUserByUsername(member.email) as CustomUserDetails

        return ReissueResult(
            accessToken =  jwtService.createAccessToken(JwtClaims.fromCustomUserDetails(userDetails))
        )
    }
}