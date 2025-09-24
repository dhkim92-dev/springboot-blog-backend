package kr.dohoonkim.blog.restapi.application.board

import kr.dohoonkim.blog.restapi.application.board.dto.article.CreatePostCommand
import kr.dohoonkim.blog.restapi.application.board.dto.article.PostDto
import kr.dohoonkim.blog.restapi.application.board.dto.article.PostSummaryDto
import kr.dohoonkim.blog.restapi.application.board.dto.article.UpdatePostCommand
import kr.dohoonkim.blog.restapi.application.board.usecases.article.CreatePostUseCase
import kr.dohoonkim.blog.restapi.application.board.usecases.article.DeletePostUseCase
import kr.dohoonkim.blog.restapi.application.board.usecases.article.QueryPostUseCase
import kr.dohoonkim.blog.restapi.application.board.usecases.article.UpdatePostUseCase
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLES_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLE_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.CATEGORIES_CACHE_KEY
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PostServiceFacade(
    private val createPostUseCase: CreatePostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val queryPostUseCase: QueryPostUseCase
) {

    @Transactional
    @Caching(
        evict = [
            CacheEvict(CATEGORIES_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLES_CACHE_KEY, allEntries = true)
        ],
        put = [CachePut(ARTICLE_CACHE_KEY, key = "#result.id")]
    )
    fun createPost(loginId: UUID, command: CreatePostCommand): UUID {
        return createPostUseCase.create(loginId, command)
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(CATEGORIES_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLES_CACHE_KEY, allEntries = true),
        ],
        put = [
            CachePut(ARTICLE_CACHE_KEY, key = "#articleId.toString()")
        ]
    )
    fun updatePost(loginId: UUID, command: UpdatePostCommand) {
        return updatePostUseCase.update(loginId, command)
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(CATEGORIES_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLES_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLE_CACHE_KEY, key = "#articleId")
        ]
    )
    fun deletePost(loginId: UUID, postId: UUID) {
        return deletePostUseCase.delete(loginId, postId)
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["article"], key = "#postId.toString()")
    fun getPost(postId: UUID): PostDto {
        return queryPostUseCase.getPost(postId)
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["articles"], unless = "#result.isEmpty()")
    fun getPosts(categoryId:Long? , cursor: UUID?, size: Int): List<PostSummaryDto> {
        return queryPostUseCase.getPosts(
            categoryId = categoryId,
            cursor = cursor,
            size = size
        )
    }
}