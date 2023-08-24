package kr.dohoonkim.blog.restapi.domain.member.repository

import kr.dohoonkim.blog.restapi.domain.member.Member
import java.util.*

interface MemberRepositoryCustom {

    fun findByMemberId(memberId : UUID) : Member?
    fun existsByEmail(email : String) : Boolean
    fun existsByNickname(nickname : String) : Boolean

}