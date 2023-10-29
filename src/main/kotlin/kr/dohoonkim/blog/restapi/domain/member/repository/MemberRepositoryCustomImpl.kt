package kr.dohoonkim.blog.restapi.domain.member.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import kr.dohoonkim.blog.restapi.common.error.ErrorCode.MEMBER_NOT_FOUND
import kr.dohoonkim.blog.restapi.common.error.exceptions.EntityNotFoundException
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.QMember.Companion.member
import java.util.*

class MemberRepositoryCustomImpl(private val queryFactory : JPAQueryFactory
) : MemberRepositoryCustom {

    override fun findByMemberId(memberId: UUID): Member {
        return queryFactory
            .selectFrom(member)
            .where(member.id.eq(memberId))
            .fetchOne()
            ?: throw EntityNotFoundException(MEMBER_NOT_FOUND)
    }

    override fun existsByNickname(nickname: String): Boolean {
        return queryFactory
            .selectOne()
            .from(member)
            .where(member.nickname.eq(nickname))
            .fetchFirst() != null
    }

    override fun existsByEmail(email: String): Boolean {
        return queryFactory
            .selectOne()
            .from(member)
            .where(member.email.eq(email))
            .fetchFirst() != null
    }

}