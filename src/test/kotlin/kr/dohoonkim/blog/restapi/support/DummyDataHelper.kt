package kr.dohoonkim.blog.restapi.support

import kr.dohoonkim.blog.restapi.application.member.dto.MemberDto
import kr.dohoonkim.blog.restapi.domain.member.Member
import kr.dohoonkim.blog.restapi.domain.member.Role
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostedArticleSummary
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostedCategorySummary
import kr.dohoonkim.blog.restapi.interfaces.member.dto.MemberSummaryVo
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime
import java.util.*


fun createMember(size: Int): List<Member> {
    return List(size) {
        val member = Member(id = UUID.randomUUID())
        member.updatePassword(BCryptPasswordEncoder(10).encode("test1234"))
        member.updateEmail("email-${it}@dohoon-kim.kr")
        member.isActivated=true
        member.role = Role.MEMBER
        member.updateNickname("nickname-${it}")
        member
    }
}

fun createMemberDtoList(size: Int): List<MemberDto> {
    return List<MemberDto>(size) { it -> MemberDto(UUID.randomUUID(), "nickname-${it}", "email-${it}@dohoon-kim.kr", Role.MEMBER.rolename, true) }
}

fun createPostedArticleDtoList(size: Int): List<PostedArticleSummary> {
    return List(size) { it ->
        PostedArticleSummary(
            id = UUID.randomUUID(),
            title = "title-${it}",
            category = PostedCategorySummary(
                id = (it % 3).toLong(),
                name = "category-${it}"
            ),
            author = MemberSummaryVo(
                id = UUID.randomUUID(),
                nickname = "nickname-${it}"
            ),
            createdAt = LocalDateTime.now(),
            viewCount = 1L,
            commentCount = 0L
        )
    }
}