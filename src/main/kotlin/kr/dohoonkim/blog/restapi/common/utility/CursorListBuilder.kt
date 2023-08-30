package kr.dohoonkim.blog.restapi.common.utility

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer
import kr.dohoonkim.blog.restapi.common.response.CursorList
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.time.format.DateTimeFormatter

/**
 * 모든 요청은 처음부터 시작한다는 가정하에 정상 동작한다.
 */
class CursorListBuilder<T> {
    companion object {
        val objectMapper = ObjectMapper().registerModule(JavaTimeModule().addSerializer(
            LocalDateTimeSerializer(
            DateTimeFormatter.ISO_DATE_TIME)
        ))

        /**
         * 주어진 커서 쿼리 키들을 기반으로 next cursor url을 만든다.
         * 현재 리퀘스트에 대해 데이터 사이즈가 pagSize + 1 보다 작다면 next 는 존재하지 않는다.
         * 데이터가 비어있다면 next는 존재하지 않는다.
         */
        fun <T> next(data: List<T>, queries: Map<String, String>, pageSize: Long, withUrl: Boolean = true): String? {
            if (data.isEmpty() || data.size < pageSize + 1) {
                return null
            }

            return getQueries(data[data.size - 1], queries, "next", withUrl)
        }

        /**
         * 현재 요청 주소 자체가 커서가 된다.
         */
        fun prev(): String? {
            return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toString()
        }

        fun <T> getQueries(target: T, queries: Map<String, String>, direction: String, withUrl: Boolean = true): String? {
            val cursor: Cursor = Cursor()
            val node = objectMapper.valueToTree<JsonNode>(target)
            var keyMap = queries.map{entry -> {
                entry.value to entry.key.split("#")
            }}

            keyMap.forEach { entry ->
                var targetNode = node
                val fields = entry().second
                var found = true

                fields.forEach { field->
                    if(!targetNode.has(field)) {
//                        println("  ${field} not exists on target")
                        found = false
                        return@forEach
                    }
                    targetNode = targetNode[field]
                }

                if (!found) {
                    return@forEach
                }
//                println("getQueries key : ${entry().first} value : ${targetNode.toString()}")
                cursor.append(entry().first, targetNode.asText())
            }
            return cursor.build(withUrl = withUrl)
        }

        fun <T> build(data: List<T>, queries: Map<String, String>, pageSize: Long, withUrl: Boolean = true): CursorList<T> {
            return CursorList.of(
                data = data,
                next = next(data, queries, pageSize, withUrl),
                pageSize = pageSize
            )
        }
    }
}