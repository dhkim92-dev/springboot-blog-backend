package kr.dohoonkim.blog.restapi.domain.member.repository

import kr.dohoonkim.blog.restapi.domain.member.OAuth2Member
import kr.dohoonkim.blog.restapi.security.oauth2.OAuth2Provider
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OAuth2MemberRepository: JpaRepository<OAuth2Member, UUID> {

    fun findByProviderAndUserId(provider: OAuth2Provider, userId: String): OAuth2Member?

    fun findByMemberIdAndProvider(memberId: UUID, provider: OAuth2Provider): OAuth2Member?

    fun existsByProviderAndUserId(provider: OAuth2Provider, userId: String): Boolean
}