package kr.dohoonkim.blog.restapi.application.board.impl.article

import kr.dohoonkim.blog.restapi.application.board.usecases.article.DeletePostUseCase
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLES_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLE_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.CATEGORIES_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.port.persistence.board.ArticleRepository
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class DeletePoseUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val articleRepository: ArticleRepository
): DeletePostUseCase {

    override fun delete(loginId: UUID, postId: UUID) {
        val member = memberRepository.findByIdOrNull(loginId)
            ?: throw ForbiddenException(ErrorCodes.NO_PERMISSION)
        val article = articleRepository.findByIdOrNull(postId)
            ?: return

        if ( !member.isAdmin() && (article.author.id != member.id) ) {
            throw ForbiddenException(ErrorCodes.NO_PERMISSION)
        }

        article.markAsDeleted()
        articleRepository.save(article)
    }
}