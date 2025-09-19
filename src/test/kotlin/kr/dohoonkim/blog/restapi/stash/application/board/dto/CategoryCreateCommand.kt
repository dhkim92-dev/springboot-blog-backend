package kr.dohoonkim.blog.restapi.stash.application.board.dto

/**
 * 카테고리 생성 요청 커맨드 DTO
 * @author dhkim92.dev
 * @since 2023.08.10
 * @property name 카테고리 이름
 */
data class CategoryCreateCommand(
    val name: String
)
