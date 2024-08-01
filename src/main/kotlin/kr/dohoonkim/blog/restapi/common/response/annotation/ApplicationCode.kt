package kr.dohoonkim.blog.restapi.common.response.annotation

import kr.dohoonkim.blog.restapi.common.response.ResultCode

/**
 * ApiResult 래핑을 위한 어노테이션
 * 이 어노테이션이 붙은 엔드포인트들 한정으로 응답 객체를 ApiResult로 래핑한다
 * @property code ResultCode
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationCode(val code: ResultCode) {

}
