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
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


/**
 * Authentication Logic
 * 1) 사용자가 Email/Password LoginRquest 를 보낸다.
 * 2) userDetailService를 통해 사용자 정보를 불러오고, Password 가 일치하는지 확인한다.
 * 3) 패스워드가 일치한다면 [CustomUserDetails] 객체로부터 Refresh token과 Access token을 생성한다.
 * 4) 사용자에게 LoginResult 응답을 전송한다.
 */
@Transactional(readOnly = true)
@Service
class AuthenticationService(
    private val jwtService: JwtService,
    private val memberRepository: MemberRepository,
    private val userDetailService: CustomUserDetailService,
    private val passwordEncoder: BCryptPasswordEncoder
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun login(request: LoginRequest): LoginResult {
        val user = userDetailService.loadUserByUsername(request.email) as CustomUserDetails

        if (!user.isEnabled) {
            throw UnauthorizedException(ErrorCode.NOT_VERIFIED_EMAIL)
        }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw BadCredentialsException("email/password mismatched.");
        }

        val claims = JwtClaims.fromCustomUserDetails(user)

        return LoginResult(
            refreshToken = jwtService.createRefreshToken(claims),
            accessToken = jwtService.createAccessToken(claims)
        )
    }

    @Transactional
    fun reIssueAccessToken(request: ReissueTokenRequest): ReissueResult {
        val jwt = jwtService.verifyRefreshToken(request.refreshToken)
        val memberId = UUID.fromString(jwt.subject)
        val member: Member = memberRepository.findByMemberId(memberId)
            ?: throw UnauthorizedException()
        val userDetails = userDetailService.loadUserByUsername(member.email) as CustomUserDetails
        val token = jwtService.createAccessToken(JwtClaims.fromCustomUserDetails(userDetails))

        return ReissueResult(accessToken = token)
    }

}