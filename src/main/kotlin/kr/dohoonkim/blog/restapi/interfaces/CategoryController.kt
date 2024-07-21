package kr.dohoonkim.blog.restapi.interfaces

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import kr.dohoonkim.blog.restapi.application.board.CategoryService
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse
import kr.dohoonkim.blog.restapi.common.response.CursorList
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.application.board.dto.*
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@ApiResponses(
    value = [
        ApiResponse(
            responseCode = "401", description = "G002 - 사용자 인증 실패",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        ),
        ApiResponse(
            responseCode = "403", description = "G003 - 이용 권한 없음",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )
    ]
)
@RequestMapping("api/")
class CategoryController(
    private val categoryService: CategoryService
) {

    @Operation(summary = "카테고리 추가", description = "게시글 카테고리를 추가합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "AC01 - 게시글 카테고리 추가 성공"),
            ApiResponse(
                responseCode = "409", description = "AC02 - 이미 존재하는 카테고리 이름입니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PostMapping("v1/article-categories")
    @ApplicationCode(ResultCode.CREATE_CATEGORY_SUCCESS)
    fun createCategory(@RequestBody @Valid request: CategoryCreateRequest): CategoryDto {
        return categoryService.createCategory(CategoryCreateDto(name=request.name))
    }

    @Operation(summary = "카테고리 목록 조회", description = "전체 게시글 카테고리를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "AC04 - 카테고리 목록을 조회하였습니다."),
        ]
    )
    @GetMapping("v1/article-categories")
    @ApplicationCode(ResultCode.GET_CATEGORIES_SUCCESS)
    fun getCategories(): CursorList<CategoryDto> {
        val categories = categoryService.getCategories()
        return CursorList(categories.size, categories, null)
    }

    @Operation(summary = "카테고리명 수정", description = "카테고리 이름을 수정합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "카테고리명 수정 성공"),
            ApiResponse(
                responseCode = "404", description = "AC02 - 카테고리 이름을 수정하였습니다.",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))]
            )
        ]
    )
    @PatchMapping("v1/article-categories/{categoryId}/name")
    @ApplicationCode(ResultCode.MODIFY_CATEGORY_NAME_SUCCESS)
    fun updateName(
        @PathVariable categoryId: Long,
        @RequestBody @Valid request: CategoryNameChangeRequest
    ): CategoryDto {
        return categoryService.modifyCategoryName(CategoryModifyDto(id = categoryId, newName = request.newName))
    }

    @Operation(summary = "카테고리명 삭제", description = "카테고리를 삭제합니다")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "카테고리 삭제"),
        ]
    )
    @DeleteMapping("v1/article-categories/{categoryId}")
    @ApplicationCode(ResultCode.DELETE_CATEGORY_SUCCESS)
    fun delete(@PathVariable categoryId: Long) {
        categoryService.deleteCategory(categoryId)
    }
}