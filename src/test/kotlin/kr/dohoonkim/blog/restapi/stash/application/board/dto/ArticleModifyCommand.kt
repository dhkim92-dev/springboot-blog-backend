package kr.dohoonkim.blog.restapi.stash.application.board.dto

/**
 * 게시믈 수정 요청 커맨드 DTO
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 * @property title 변경 제목
 * @property contains 변경 본문
 * @property category 변경 카테고리
 */
data class ArticleModifyCommand(
    val title: String?,
    val contents: String?,
    val category: String?
)
