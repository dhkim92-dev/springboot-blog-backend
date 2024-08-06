package kr.dohoonkim.blog.restapi.support.web

//import utility.JwtServiceTest.Companion.jwtConfig

//fun createLoginResult(member : Member) : LoginResult{
//    val jwtService = JwtService(jwtConfig)
//    val userDetails = CustomUserDetails.from(member)
//    val claims = JwtClaims.fromCustomUserDetails(userDetails)
//
//    return LoginResult(
//        refreshToken = jwtService.createRefreshToken(claims),
//        accessToken = jwtService.createAccessToken(claims)
//    )
//}

//fun createLoginResultResponse(result : LoginResult) : ResponseEntity<ApiResult<LoginResult>> {
//    return ResponseEntity(ApiResult.Ok(ResultCode.AUTHENTICATION_SUCCESS, result), ResultCode.AUTHENTICATION_SUCCESS.status)
//}