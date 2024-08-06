package kr.dohoonkim.blog.restapi.application.board.dto

/**
 * 카테고리 요약 반환 객체
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 * @property id 카테고리 ID
 * @property name 카테고리 이름
 */
class CategorySummaryDto(
    val id: Long,
    val name: String
) {
}