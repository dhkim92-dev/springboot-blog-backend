package kr.dohoonkim.blog.restapi.application.board.impl

import kr.dohoonkim.blog.restapi.application.board.CategoryService
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryCreateCommand
import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.exceptions.ConflictException
import kr.dohoonkim.blog.restapi.common.error.exceptions.EntityNotFoundException
import kr.dohoonkim.blog.restapi.domain.article.repository.CategoryRepository
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryDto
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryModifyCommand
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLES_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.ARTICLE_CACHE_KEY
import kr.dohoonkim.blog.restapi.common.constants.CacheKey.Companion.CATEGORIES_CACHE_KEY
import kr.dohoonkim.blog.restapi.domain.article.Category
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * CategoryService 구현체
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 */
@Service
@Transactional(readOnly = true)
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository
) : CategoryService {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Transactional
    @CacheEvict(CATEGORIES_CACHE_KEY, allEntries = true)
    override fun createCategory(request: CategoryCreateCommand): CategoryDto {
        if (this.categoryRepository.existsByName(request.name)) {
            throw ConflictException(ErrorCode.ALREADY_EXIST_CATEGORY)
        }

        val category = Category(name = request.name)
        return CategoryDto.fromEntity(this.categoryRepository.save(category))
    }

    @Transactional
    @Cacheable(CATEGORIES_CACHE_KEY)
    override fun getCategories(): List<CategoryDto> {
        return categoryRepository.findAllCategory()
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(CATEGORIES_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLE_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLES_CACHE_KEY, allEntries = true)
        ]
    )
    override fun modifyCategoryName(request: CategoryModifyCommand): CategoryDto {
        val category = categoryRepository.findById(request.id)
            .orElseThrow { throw EntityNotFoundException(ErrorCode.CATEGORY_NOT_FOUND) }

        if (categoryRepository.existsByName(request.newName)) {
            throw ConflictException(ErrorCode.ALREADY_EXIST_CATEGORY)
        }

        category.changeName(request.newName)

        return CategoryDto.fromEntity(categoryRepository.save(category))
    }

    @Transactional
    @Caching(
        evict = [
            CacheEvict(CATEGORIES_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLE_CACHE_KEY, allEntries = true),
            CacheEvict(ARTICLES_CACHE_KEY, allEntries = true)
        ]
    )
    override fun deleteCategory(categoryId: Long): Unit {
        categoryRepository.deleteById(categoryId)
    }
}