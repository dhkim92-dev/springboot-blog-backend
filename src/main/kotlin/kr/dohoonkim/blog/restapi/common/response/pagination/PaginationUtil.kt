package kr.dohoonkim.blog.restapi.common.response.pagination

import kr.dohoonkim.blog.restapi.common.response.CursorList
import kr.dohoonkim.blog.restapi.common.utility.UrlUtility
import org.slf4j.LoggerFactory
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.lang.IllegalStateException
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * PaginationUtil class
 * 요청에 대한 다음 커서를 변환하는 책임을 담당한다
 */
@Component
class PaginationUtil(private val urlUtility: UrlUtility) {

    val logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * 주어진 리스트를 커서 리스트로 반환하는 역할을 담당한다.
     * @param data 컨트롤러에서 반환한 List 데이터
     * @param parameters Cursor 어노테이션이 적용된 메소드 파라미터 리스트
     * @param queries 현재 요청의 쿼리 스트링 맵
     */
    fun toCursorList(data: List<Any?>, parameters: List<MethodParameter>, queries: Map<String, String>): CursorList<Any?> {
        val emptyCursorList = CursorList<Any?>(0, emptyList(), null)
        if(data.isEmpty()) return emptyCursorList

        val sb = CursorBuilder()
        val lastElement = data.lastOrNull()!!

        for(param in parameters) {
            val annotation = param.getParameterAnnotation(Cursor::class.java)
                ?: return emptyCursorList
            val queryKey = param.parameterName!!
            val fieldKey = if(annotation.key=="") queryKey else annotation.key

            if(annotation.inherit) {
                queries[queryKey]?.let {
                    sb.append(queryKey, it)
                }
                continue
            }

            val obj = getNestedField(lastElement, fieldKey)
            if(obj != null) {
                sb.append(queryKey, obj.toString())
            }
        }

        var sizeValue = sb.get("size") ?: queries["size"]
        val targetSize = (sizeValue as? String)?.toIntOrNull() ?: 20
        sb.append("size", targetSize)
        var next: String? =  if(data.size > targetSize) sb.build(urlUtility.getURL()) else null

        return CursorList.of(data, next, targetSize.toLong())
    }

    /**
     * Object의 내부 필드까지 탐색하여 값을 반환한다.
     * @param obj 탐색 대상 Object
     * @param path 탐색 경로
     */
    fun getNestedField(obj: Any, path: String): Any? {
        val keys = path.split(".")
        var currentObj: Any? = obj

        for(key in keys) {
            if(currentObj == null) return null
            val properties = currentObj::class.memberProperties
            val property = properties.find { it.name == key }
                    as? KProperty1<Any, *>
                ?: return null
            property.isAccessible = true
            currentObj = property.get(currentObj)
        }

        return currentObj
    }
}