package kr.dohoonkim.blog.restapi.port.persistence.board.dto

import kr.dohoonkim.blog.restapi.application.board.dto.category.CategoryDto
import kr.dohoonkim.blog.restapi.application.member.dto.MemberSummaryDto
import java.time.LocalDateTime
import java.util.UUID

class ArticleQueryModel(
    val id: UUID = UUID.randomUUID(),
    val author: MemberSummaryDto = MemberSummaryDto(),
    val category: CategoryDto = CategoryDto(),
    val title: String = "",
    val content: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val viewCount: Long = 0L,
    val replyCount: Long = 0L,
    val likeCount: Long = 0L
) {
}