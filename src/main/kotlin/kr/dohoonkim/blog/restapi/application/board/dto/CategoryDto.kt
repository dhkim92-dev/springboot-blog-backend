package kr.dohoonkim.blog.restapi.application.board.dto

import kr.dohoonkim.blog.restapi.domain.article.Category

/**
 * 카테고리 객체
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 * @property id 카테고리 ID
 * @property name 카테고리 이름
 * @property count 카테고리에 속한 게시글 갯수
 */
class CategoryDto(
    val id: Long,
    val name: String,
    val count: Long = 0
) {
    companion object {
        fun fromEntity(category: Category) = CategoryDto(
            id = category.id!!,
            name = category.name,
            count = category.articles.size.toLong()
        )
    }
}
