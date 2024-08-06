package kr.dohoonkim.blog.restapi.application.board

import kr.dohoonkim.blog.restapi.application.board.dto.*
import kr.dohoonkim.blog.restapi.common.error.exceptions.ForbiddenException
import java.time.LocalDateTime
import java.util.*

/**
 * 게시물 CRUD를 담당하는 서비스 인터페이스
 * @author dhkim92.dev@gmail.com
 * @since 2023.08.10
 */
interface ArticleService {

    /**
     * 게시물을 작성한다.
     * <p>
     * 관리자만 사용 가능해야하기 때문에 이 메소드를 호출하는 엔드포인트에서는 반드시 ROLE_ADMIN 권한을 요구해야한다
     * </p>
     * @param memberId 요청 사용자 ID
     * @param request 게시물 생성 요청 정보
     * @return 본문을 포함한 게시물 정보
     */
    fun createArticle(memberId : UUID, request: ArticleCreateCommand): ArticleDto

    /**
     * 게시물의 제목, 카테고리, 본문 내용을 수정한다.
     * @param memberId 요청 사용자 ID
     * @param articleId 게시글 ID
     * @param request 수정된 게시물 데이터
     * @return 본문을 포함한 게시물 정보
     * @throws EntityNotFoundException 게시물이 존재하지 않거나, 카테고리가 존재하지 않음
     * @throws ForbiddenException 게시물의 작성자가 일치하지 않음
     */
    fun modifyArticle(memberId : UUID, articleId: UUID, request: ArticleModifyCommand): ArticleDto

    /**
     * 게시물을 삭제한다.
     * @param memberId 요청 사용자 ID
     * @param articleId 삭제 대상 게시물 ID
     * @throws ForbiddenException 게시물의 작성자가 일치하지 않음
     */
    fun deleteArticle(memberId : UUID, articleId: UUID): Unit

    /**
     * 게시물 목록을 반환한다.
     * @param categoryId 조회 대상 게시글 카테고리
     * @param cursor 페이지네이션 기준, cursor 시점을 포함한 이전 게시물들만 조회된다.
     * @param pageSize 페이지네이션 사이즈. 데이터베이스 조회는 pageSize + 1만큼 조회된다.
     * @return 게시물 본문을 포함하지 않는 게시물 정보 리스트 반환, 최대 크기는 pageSize 이다.
     */
    fun getListOfArticles(categoryId: Long, cursor: LocalDateTime?, pageSize: Int): List<ArticleSummaryDto>

    /**
     * 게시물 ID를 가지고 게시물을 조회하여 게시물을 반환한다.
     * @param articleId 게시글 ID
     * @return 게시물 본문을 포함한 게시글 정보
     */
    fun getArticle(articleId: UUID): ArticleDto
}