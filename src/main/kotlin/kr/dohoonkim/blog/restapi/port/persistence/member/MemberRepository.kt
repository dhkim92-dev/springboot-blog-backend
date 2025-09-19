package kr.dohoonkim.blog.restapi.port.persistence.member

import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.OAuth2Provider
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MemberRepository : JpaRepository<Member, UUID>, MemberRepositoryCustom {

    fun findByEmail(email : String) : Member?

    @Query(
        """
        SELECT m FROM Member m
        JOIN OAuth2Member o ON m.id = o.member.id
        WHERE o.provider = :provider AND o.userId = :userId
        """
    )
    fun findByOAuth2Info(provider: OAuth2Provider, userId: String): Member?
}