package kr.dohoonkim.blog.restapi.application.board.dto

import jakarta.validation.constraints.NotBlank

/**
 * 게시물 생성 요청 커맨드 DTO
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 * @property title 게시물 제목
 * @property contents 게시물 본문
 * @property category 카테고리 이름
 */
data class ArticleCreateCommand(
    val title: String,
    val contents: String,
    val category: String
)
