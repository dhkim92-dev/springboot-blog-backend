package kr.dohoonkim.blog.restapi.application.board.dto

import kr.dohoonkim.blog.restapi.domain.article.Category

data class CategoryDto(
        val id : Long,
        val name : String,
        val count : Long = 0
){
    companion object {
        fun fromEntity(category : Category) = CategoryDto(
                id = category.id!!,
                name = category.name,
                count = category.articles.size.toLong()
        )
    }
}
