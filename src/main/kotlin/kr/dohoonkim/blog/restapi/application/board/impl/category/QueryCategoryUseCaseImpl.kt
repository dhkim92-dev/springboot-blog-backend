package kr.dohoonkim.blog.restapi.application.board.impl.category

import kr.dohoonkim.blog.restapi.application.board.dto.category.CategoryDto
import kr.dohoonkim.blog.restapi.application.board.usecases.category.QueryCategoryUseCase
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.CATEGORIES_CACHE_KEY
import kr.dohoonkim.blog.restapi.port.persistence.board.CategoryRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class QueryCategoryUseCaseImpl(
    private val categoryRepository: CategoryRepository
): QueryCategoryUseCase {

    @Cacheable(CATEGORIES_CACHE_KEY)
    override fun getCategories(): List<CategoryDto> {
        return categoryRepository.findAll()
            .asSequence()
            .map{ CategoryDto.from(it) }
            .toList()
    }
}