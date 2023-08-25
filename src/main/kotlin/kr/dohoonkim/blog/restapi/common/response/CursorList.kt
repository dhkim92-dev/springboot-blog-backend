package kr.dohoonkim.blog.restapi.common.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CursorList<T>(val count : Int,
                         val data : List<T>,
                         var prev : String?,
                         var next : String?) {
    companion object {
        fun <T> of(data : List<T>, next : String?, prev : String?, pageSize : Long)
        : CursorList<T> {
            if(data.isEmpty()) {
                return CursorList<T>(0, emptyList(), null, null)
            }

            var count : Int = if(pageSize + 1 <= data.size) pageSize.toInt() else data.size.toInt()

            return CursorList<T>(count, data.subList(0, count), prev, next)
        }
    }
}
