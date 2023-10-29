package kr.dohoonkim.blog.restapi.common.utility

import kr.dohoonkim.blog.restapi.application.authentication.dto.JwtClaims
import kr.dohoonkim.blog.restapi.common.error.ErrorCode.*
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.Role
import kr.dohoonkim.blog.restapi.domain.member.repository.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthenticationUtil(private val memberRepository: MemberRepository) {

    private val log = LoggerFactory.getLogger(javaClass)

    fun extractAuthenticationMember(): Member {
        val authentication = SecurityContextHolder.getContext().authentication
        val claims = authentication.principal as JwtClaims

        return this.memberRepository.findByMemberId(claims.id)
    }

    fun extractMemberId(): UUID {
        val authentication = SecurityContextHolder.getContext().authentication
        val claims = authentication.principal as JwtClaims

        return if (claims.isActivated) claims.id else throw UnauthorizedException(NOT_VERIFIED_EMAIL)
    }

    fun checkResourceOwner(memberId: UUID, resourceOwnerId: UUID): Boolean {
        return resourceOwnerId == memberId
    }

    fun isAdmin(): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication.principal as JwtClaims

        return authentication.roles.contains(Role.ADMIN.rolename)
    }

    fun checkPermission(memberId: UUID, resourceOwnerId: UUID) {
        if (!isAdmin() && !checkResourceOwner(memberId, resourceOwnerId)) {
            throw ForbiddenException(RESOURCE_OWNERSHIP_VIOLATION)
        }
    }
}