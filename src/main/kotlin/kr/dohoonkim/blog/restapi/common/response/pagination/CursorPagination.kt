package kr.dohoonkim.blog.restapi.common.response.pagination

/**
 * CursorPagination Annotation
 * 위 커서가 포함되어 있다면 CursorList 반환의 대상이 된다.
 * @see PaginationUtil
 * @see Cursor
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CursorPagination() {

}
