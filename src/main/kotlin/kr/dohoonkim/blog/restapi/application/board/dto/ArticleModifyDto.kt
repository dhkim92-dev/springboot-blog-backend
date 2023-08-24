package kr.dohoonkim.blog.restapi.application.board.dto

import java.util.*

data class ArticleModifyDto(
    val articleId : UUID,
    val title : String,
    val contents : String,
    val category : String
)
