package kr.dohoonkim.blog.restapi.application.board.impl.category

import kr.dohoonkim.blog.restapi.application.board.usecases.category.DeleteCategoryUseCase
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLES_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLE_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.CATEGORIES_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.port.persistence.board.CategoryRepository
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class DeleteCategoryUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository
): DeleteCategoryUseCase {

    override fun delete(loginId: UUID, categoryId: Long) {
        val member = memberRepository.findByIdOrNull(loginId)
            ?: throw ForbiddenException(ErrorCodes.NO_PERMISSION)
        if ( !member.isAdmin() ) {
            throw ForbiddenException(ErrorCodes.NO_PERMISSION)
        }
        categoryRepository.deleteById(categoryId)
    }
}