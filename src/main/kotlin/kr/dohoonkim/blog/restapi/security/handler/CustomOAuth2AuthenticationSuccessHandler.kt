package kr.dohoonkim.blog.restapi.security.handler

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.application.authentication.CustomUserDetailService
import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginResult
import kr.dohoonkim.blog.restapi.application.authentication.vo.MemberProfile
import kr.dohoonkim.blog.restapi.application.member.MemberService
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import kr.dohoonkim.blog.restapi.security.jwt.JwtAuthentication
import kr.dohoonkim.blog.restapi.security.jwt.JwtService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class CustomOAuth2AuthenticationSuccessHandler(
    private val jwtService: JwtService,
//    private val customUserDetailService: CustomUserDetailService,
//    private val memberService: MemberService,
    private val memberRepository: MemberRepository,
    private val objectMapper: ObjectMapper
) : SimpleUrlAuthenticationSuccessHandler() {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2User = authentication.principal as MemberProfile
//        val userDetail = customUserDetailService.loadUserByUsername(oAuth2User.email) as JwtAuthentication
        val member = memberRepository.findByEmail(oAuth2User.email)!!
//        val jwtAuthentication = JwtAuthentication.fromMember(member)
        val accessToken = jwtService.createAccessToken(member)
        val refreshToken = jwtService.createRefreshToken(member)
        val body = ApiResult.Ok(ResultCode.AUTHENTICATION_SUCCESS, LoginResult(refreshToken=refreshToken, accessToken = accessToken))

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.status = HttpStatus.CREATED.value()
        response.writer.write(objectMapper.writeValueAsString(body))
        response.writer.flush()
    }
}