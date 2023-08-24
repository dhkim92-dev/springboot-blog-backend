package kr.dohoonkim.blog.restapi.support.web

import kr.dohoonkim.blog.restapi.application.authentication.dto.JwtClaims
import kr.dohoonkim.blog.restapi.application.authentication.dto.LoginResult
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.config.security.jwt.JwtService
import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.units.utility.JwtServiceTest.Companion.jwtConfig
import org.springframework.http.ResponseEntity

fun createLoginResult(member : Member) : LoginResult{
    val jwtService = JwtService(jwtConfig)
    val userDetails = CustomUserDetails.from(member)
    val claims = JwtClaims.fromCustomUserDetails(userDetails)

    return LoginResult(
        refreshToken = jwtService.createRefreshToken(claims),
        accessToken = jwtService.createAccessToken(claims)
    )
}

fun createLoginResultResponse(result : LoginResult) : ResponseEntity<ApiResult<LoginResult>> {
    return ApiResult.Ok(ResultCode.AUTHENTICATION_SUCCESS, result)
}