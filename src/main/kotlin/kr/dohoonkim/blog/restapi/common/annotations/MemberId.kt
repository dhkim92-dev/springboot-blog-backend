package kr.dohoonkim.blog.restapi.common.annotations

import io.swagger.v3.oas.annotations.Parameter

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@Parameter(hidden = true)
annotation class MemberId(
    val description: String = "MemberIdRequired annotation"
)
