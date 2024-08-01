package kr.dohoonkim.blog.restapi.application.board

import kr.dohoonkim.blog.restapi.application.board.dto.CategoryCreateCommand
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryDto
import kr.dohoonkim.blog.restapi.application.board.dto.CategoryModifyCommand

/**
 * 게시물 카테고리 CRUD를 담당하는 서비스 인터페이스
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 */
interface CategoryService {

    /**
     * 카테고리를 신규 생성한다
     * <p>관리자 전용 기능</p>
     * @param request 카테고리 생성 요청 본문
     * @return 카테고리 전체 정보를 포함한 데이터
     */
    fun createCategory(request: CategoryCreateCommand): CategoryDto

    /**
     * 카테고리 이름을 수정한다
     * @param request 카테고리 수정 요청 본문
     * @return 카테고리 전체 정보를 포함한 데이터
     */
    fun modifyCategoryName(request: CategoryModifyCommand): CategoryDto

    /**
     * 카테고리 목록을 반환한다
     * @return 전체 카테고리 목록 리스트
     */
    fun getCategories(): List<CategoryDto>

    /**
     * 카테고리를 삭제한다
     * @param categoryId 대상 카테고리 ID
     */
    fun deleteCategory(categoryId: Long): Unit
}