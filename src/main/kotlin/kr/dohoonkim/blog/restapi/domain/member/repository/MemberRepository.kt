package kr.dohoonkim.blog.restapi.domain.member.repository

import kr.dohoonkim.blog.restapi.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface MemberRepository : JpaRepository<Member, UUID>, MemberRepositoryCustom {

    fun findByEmail(email : String) : Member?

}