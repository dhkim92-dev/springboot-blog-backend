package kr.dohoonkim.blog.restapi.application.board.dto.category

import kr.dohoonkim.blog.restapi.domain.board.Category

data class CategoryDto(
    val id: Long = 0L,
    val name: String = "",
    val count: Long = 0L,
) {

    companion object {
        fun from(entity: Category) = CategoryDto(
            id = entity.id!!,
            name = entity.name,
            count = entity.count
        )
    }
}