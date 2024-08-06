package kr.dohoonkim.blog.restapi.common.utility

import kr.dohoonkim.blog.restapi.application.authentication.vo.JwtClaims
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes.*
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.UnauthorizedException
import kr.dohoonkim.blog.restapi.domain.member.Role
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

/**
 * 인증 정보 추출 및 권한 체크 등의 책임을 갖는 클래스
 */
@Component
class AuthenticationUtil {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * 사용자 인증 정보가 존재하는 경우 사용자 id 반환
     */
    fun extractMemberId(): UUID {
        val authentication = SecurityContextHolder.getContext().authentication
        val claims = authentication.principal as JwtClaims

        return if (claims.isActivated) claims.id
        else throw UnauthorizedException(NOT_VERIFIED_EMAIL)
    }

    /**
     *
     */
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