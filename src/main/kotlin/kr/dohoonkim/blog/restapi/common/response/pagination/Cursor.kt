package kr.dohoonkim.blog.restapi.common.response.pagination

import org.springframework.web.bind.annotation.RequestParam

/**
 * Cursor
 * @param key 어노테이션이 붙은 RequestParam이 실제 반환 데이터의 어떤 필드의 값으로 대치될 것인지 표기
 * @param inherit 현재 리퀘스트에 적용된 쿼리 값을 상속할 것인지 여부
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Cursor(val key: String="", val inherit: Boolean=false){

}