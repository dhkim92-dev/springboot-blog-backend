package kr.dohoonkim.blog.restapi.application.authentication.dto

import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import java.util.UUID

data class JwtClaims(
        val id : UUID,
        val email : String,
        val nickname : String,
        val roles : Array<String>,
        val isActivated : Boolean){

    companion object {
        fun fromCustomUserDetails(detail : CustomUserDetails) : JwtClaims {
            return JwtClaims(
                    detail.memberId,
                    detail.email,
                    detail.nickname,
                    arrayOf(detail.role.rolename),
                    detail.isActivated
            )
        }
    }
}