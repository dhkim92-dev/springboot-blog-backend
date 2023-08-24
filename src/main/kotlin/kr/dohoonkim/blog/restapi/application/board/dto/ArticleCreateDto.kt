package kr.dohoonkim.blog.restapi.application.board.dto

data class ArticleCreateDto(
    val title : String,
    val contents : String,
    val category : String
)
