package kr.dohoonkim.blog.restapi.port.persistence.member

import kr.dohoonkim.blog.restapi.domain.member.Member
import java.util.*

interface MemberRepositoryCustom {

    fun findByMemberId(memberId : UUID) : Member

    fun existsByEmail(email : String) : Boolean
    
    fun existsByNickname(nickname : String) : Boolean

}