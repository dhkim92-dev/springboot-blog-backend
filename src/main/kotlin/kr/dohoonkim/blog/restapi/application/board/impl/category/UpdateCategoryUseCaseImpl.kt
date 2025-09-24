package kr.dohoonkim.blog.restapi.application.board.impl.category

import kr.dohoonkim.blog.restapi.application.board.dto.category.UpdateCategoryCommand
import kr.dohoonkim.blog.restapi.application.board.usecases.category.UpdateCategoryUseCase
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
class UpdateCategoryUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository
): UpdateCategoryUseCase {

    override fun update(loginId: UUID, command: UpdateCategoryCommand) {
        val member = memberRepository.findByIdOrNull(loginId)
            ?: throw ForbiddenException(ErrorCodes.NO_PERMISSION)
        val category = categoryRepository.findByIdOrNull(command.id)
            ?: throw NotFoundException(ErrorCodes.CATEGORY_NOT_FOUND)
        category.changeName(member, command.name)
        categoryRepository.save(category)
    }
}