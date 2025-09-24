package kr.dohoonkim.blog.restapi.application.board.impl.category

import kr.dohoonkim.blog.restapi.application.board.dto.category.CategoryDto
import kr.dohoonkim.blog.restapi.application.board.dto.category.CreateCategoryCommand
import kr.dohoonkim.blog.restapi.application.board.usecases.category.CreateCategoryUseCase
import kr.dohoonkim.blog.restapi.common.constants.CacheKey
import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.exceptions.ConflictException
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import kr.dohoonkim.blog.restapi.domain.board.Category
import kr.dohoonkim.blog.restapi.port.persistence.board.CategoryRepository
import kr.dohoonkim.blog.restapi.port.persistence.member.MemberRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class CreateCategoryUseCaseImpl(
    private val memberRepository: MemberRepository,
    private val categoryRepository: CategoryRepository
): CreateCategoryUseCase {

    override fun create(loginId: UUID, command: CreateCategoryCommand): CategoryDto {
        val member = memberRepository.findByIdOrNull(loginId)
            ?: throw ForbiddenException(ErrorCodes.NO_PERMISSION.code, "로그인한 회원 정보가 존재하지 않습니다.")

        if ( categoryRepository.existsByName(command.name) ) {
            throw ConflictException(ErrorCodes.ALREADY_EXIST_CATEGORY)
        }

        val category = Category.create(member, command.name)

        return CategoryDto.from(categoryRepository.save(category))
    }
 }