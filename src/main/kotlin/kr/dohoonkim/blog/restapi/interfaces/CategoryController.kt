package kr.dohoonkim.blog.restapi.interfaces

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import kr.dohoonkim.blog.restapi.application.board.CategoryService
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.ApiResult.Companion.Ok
import kr.dohoonkim.blog.restapi.common.response.CursorList
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.application.board.dto.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@ApiResponses(value = [
    ApiResponse(responseCode = "401", description = "G002 - 사용자 인증 실패",
            content = [Content(schema = Schema(implementation= ErrorResponse::class))]),
    ApiResponse(responseCode = "403", description = "G003 - 이용 권한 없음",
            content = [Content(schema = Schema(implementation= ErrorResponse::class))])
])
@RequestMapping("api/")
class CategoryController(
        private val categoryService: CategoryService
){

    private val DEFAULT_PAGINATION_SIZE = 100L

    @Operation(summary = "카테고리 추가", description = "게시글 카테고리를 추가합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "AC01 - 게시글 카테고리 추가 성공"),
        ApiResponse(responseCode = "409", description = "AC02 - 이미 존재하는 카테고리 이름입니다.",
            content = [Content(schema = Schema(implementation=ErrorResponse::class))])
    ])
    @PostMapping("v1/article-categories")
    fun create(@RequestBody @Valid request : CategoryCreateRequest) : ResponseEntity<ApiResult<CategoryDto>> {
        val dto = CategoryCreateDto(name = request.name)

        return Ok(ResultCode.CREATE_CATEGORY_SUCCESS, categoryService.createCategory(dto))
    }

    @Operation(summary = "카테고리 목록 조회", description = "전체 게시글 카테고리를 조회합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "AC04 - 카테고리 목록을 조회하였습니다."),
    ])
    @GetMapping("v1/article-categories")
    fun list(): ResponseEntity<ApiResult<CursorList<CategoryDto>>> {
        val categories = categoryService.getCategories()
        val ret = CursorList<CategoryDto>(categories.size, categories, null)

        return Ok(ResultCode.GET_CATEGORIES_SUCCESS, ret)
    }

    @Operation(summary = "카테고리명 수정", description = "카테고리 이름을 수정합니다.")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "카테고리명 수정 성공"),
        ApiResponse(responseCode = "404", description = "AC02 - 카테고리 이름을 수정하였습니다.",
            content = [Content(schema = Schema(implementation=ErrorResponse::class))])
    ])
    @PatchMapping("v1/article-categories/{categoryId}/name")
    fun updateName(@PathVariable categoryId : Long, @RequestBody @Valid request : CategoryNameChangeRequest) : ResponseEntity<ApiResult<CategoryDto>> {
        val dto = CategoryModifyDto(id = categoryId, newName = request.newName)

       return Ok(ResultCode.MODIFY_CATEGORY_NAME_SUCCESS, categoryService.modifyCategoryName(dto))
    }

    @Operation(summary = "카테고리명 삭제", description = "카테고리를 삭제합니다")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "카테고리 삭제"),
    ])
    @DeleteMapping("v1/article-categories/{categoryId}")
    fun delete(@PathVariable categoryId : Long) = Ok(ResultCode.DELETE_CATEGORY_SUCCESS, categoryService.deleteCategory(categoryId))


}