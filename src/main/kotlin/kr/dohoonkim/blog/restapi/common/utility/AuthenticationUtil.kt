package kr.dohoonkim.blog.restapi.common.utility

import kr.dohoonkim.blog.restapi.application.authentication.dto.JwtClaims
import kr.dohoonkim.blog.restapi.security.authentication.JwtAuthentication
import kr.dohoonkim.blog.restapi.domain.member.CustomUserDetails
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.Role
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthenticationUtil(private val memberRepository: MemberRepository){

    private val log = LoggerFactory.getLogger(javaClass)
    fun extractAuthenticationMember() : Member? {
        log.debug("extractAuthenticationMember")
        val authentication = SecurityContextHolder.getContext().authentication
        val claims = authentication.principal as JwtClaims
        log.debug("claims : ${claims}")
        return this.memberRepository.findByMemberId(claims.id) ?: null
    }

    fun extractMemberId() : UUID? {
        val authentication = SecurityContextHolder.getContext().authentication
        val claims = authentication.principal as JwtClaims
        return if(claims.isActivated) claims.id else null
    }

    fun isResourceOwner(memberId : UUID) : Boolean{
        val authentication = SecurityContextHolder.getContext().authentication.principal as JwtClaims
        return authentication.id == memberId
    }

    fun isAdmin() : Boolean{
        val authentication = SecurityContextHolder.getContext().authentication.principal as JwtClaims
        return authentication.roles.contains(Role.ADMIN.rolename)
    }
}