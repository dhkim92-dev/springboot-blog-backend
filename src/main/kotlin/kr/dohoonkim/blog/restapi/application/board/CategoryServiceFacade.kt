package kr.dohoonkim.blog.restapi.application.board

import kr.dohoonkim.blog.restapi.application.board.dto.category.CreateCategoryCommand
import kr.dohoonkim.blog.restapi.application.board.dto.category.UpdateCategoryCommand
import kr.dohoonkim.blog.restapi.application.board.usecases.category.CreateCategoryUseCase
import kr.dohoonkim.blog.restapi.application.board.usecases.category.DeleteCategoryUseCase
import kr.dohoonkim.blog.restapi.application.board.usecases.category.QueryCategoryUseCase
import kr.dohoonkim.blog.restapi.application.board.usecases.category.UpdateCategoryUseCase
import kr.dohoonkim.blog.restapi.common.constants.CacheKey
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLES_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLE_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.CATEGORIES_CACHE_KEY
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CategoryServiceFacade(
    private val createCategoryUseCase: CreateCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val queryCategoryUseCase: QueryCategoryUseCase
) {

    @Transactional
    @CacheEvict(CacheKey.CATEGORIES_CACHE_KEY, allEntries = true)
    fun create(loginId: UUID, command: CreateCategoryCommand) = createCategoryUseCase.create(loginId, command)

    @Transactional
    @Caching(
        evict = [
            CacheEvict(CATEGORIES_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLE_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLES_CACHE_KEY, allEntries = true)
        ]
    )
    fun update(loginId: UUID, command: UpdateCategoryCommand) = updateCategoryUseCase.update(loginId, command)

    @Transactional
    @Caching(
        evict = [
            CacheEvict(CATEGORIES_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLE_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLES_CACHE_KEY, allEntries = true)
        ]
    )
    fun delete(loginId: UUID, categoryId: Long) = deleteCategoryUseCase.delete(loginId, categoryId)

    @Transactional(readOnly=true)
    @Cacheable(CATEGORIES_CACHE_KEY)
    fun getCategories() = queryCategoryUseCase.getCategories()
}