package kr.dohoonkim.blog.restapi.common.response.annotation

import kr.dohoonkim.blog.restapi.common.response.ResultCode

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationCode(val code: ResultCode) {

}
