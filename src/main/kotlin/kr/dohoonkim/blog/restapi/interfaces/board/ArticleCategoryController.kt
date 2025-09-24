package kr.dohoonkim.blog.restapi.interfaces.board

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import kr.dohoonkim.blog.restapi.application.board.CategoryServiceFacade
import kr.dohoonkim.blog.restapi.common.annotations.MemberId
import kr.dohoonkim.blog.restapi.common.response.ListResponse
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import kr.dohoonkim.blog.restapi.interfaces.board.dto.CategoryResponse
import kr.dohoonkim.blog.restapi.interfaces.board.dto.CreateCategoryRequest
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/article-categories")
@Tag(name="게시물 카테고리 API")
class ArticleCategoryController(
    private val categoryService: CategoryServiceFacade
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새로운 게시물 카테고리를 생성합니다.")
    @ApplicationCode(ResultCode.CREATE_CATEGORY_SUCCESS)
    @SecurityRequirement(name = "bearer-jwt")
    fun postCategory(
        @MemberId loginId: UUID,
        @RequestBody @Valid request: CreateCategoryRequest
    ): CategoryResponse {
        logger.debug("request.name : ${request.name}")
        val category = categoryService.create(
            loginId,
            request.toCommand()
        )
        return CategoryResponse.from(category)
    }

    @PutMapping("{categoryId}")
    @Operation(summary = "카테고리 수정", description = "기존 게시물 카테고리를 수정합니다.")
    @ApplicationCode(ResultCode.MODIFY_CATEGORY_NAME_SUCCESS)
    @SecurityRequirement(name = "bearer-jwt")
    fun putCategory(
        @MemberId loginId: UUID,
        @PathVariable categoryId: Long,
        @RequestBody @Valid request: CreateCategoryRequest
    ) {
        categoryService.update(
            loginId,
            request.toCommand(categoryId)
        )
    }

    @DeleteMapping("{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "기존 게시물 카테고리를 삭제합니다.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearer-jwt")
    fun deleteCategory(
        @MemberId loginId: UUID,
        @PathVariable categoryId: Long
    ) {
        categoryService.delete(
            loginId,
            categoryId
        )
    }

    @GetMapping("")
    @Operation(summary = "카테고리 목록 조회", description = "전체 게시물 카테고리 목록을 조회합니다.")
    @ApplicationCode(ResultCode.GET_CATEGORIES_SUCCESS)
    fun getCategories(
    ): ListResponse<CategoryResponse> {
        val categories = categoryService.getCategories()
            .map { CategoryResponse.from(it) }
        return ListResponse.from(categories)
    }
}