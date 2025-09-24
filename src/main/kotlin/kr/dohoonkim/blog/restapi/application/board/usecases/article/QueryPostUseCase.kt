package kr.dohoonkim.blog.restapi.application.board.usecases.article

import kr.dohoonkim.blog.restapi.application.board.dto.article.PostDto
import kr.dohoonkim.blog.restapi.application.board.dto.article.PostSummaryDto
import java.util.UUID

interface QueryPostUseCase {

    fun getPost(postId: UUID): PostDto

    fun getPosts(categoryId: Long?, cursor: UUID?, size: Int): List<PostSummaryDto>
}