package kr.dohoonkim.blog.restapi.application.board.dto


/**
 * 게시물 수정 요청 커맨드 DTO
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 * @property id 카테고리 ID
 * @property newName 변경될 카테고리 이름
 */
data class CategoryModifyCommand(
    val id: Long,
    val newName: String
)
