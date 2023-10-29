package kr.dohoonkim.blog.restapi.security.annotations

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class MemberId(
    val description: String = "MemberIdRequired annotation"
)
