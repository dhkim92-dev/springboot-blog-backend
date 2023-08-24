package kr.dohoonkim.blog.restapi.common.utility

import kr.dohoonkim.blog.restapi.common.response.CursorList
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMembers
import kotlin.reflect.jvm.isAccessible

class CursorListBuilder<T> {

    companion object {
        /**
         * 주어진 커서 쿼리 키들을 기반으로 next cursor url을 만든다.
         * 현재 리퀘스트에 대해 데이터 사이즈가 pagSize + 1 보다 작다면 next 는 존재하지 않는다.
         * 데이터가 비어있다면 next는 존재하지 않는다.
         */
        fun <T> next(data: List<T>, queries: List<String>, pageSize: Long, withUrl: Boolean = true): String? {
            if (data.isEmpty() || data.size < pageSize + 1) {
                return null
            }

            return getQueries(data[data.size - 1], queries, "next", withUrl)
        }

        /**
         * 주어진 쿼리 키들을 기반으로 prev cursor url을 만든다.
         * 현재 요청에 대해 데이터가 없다면 prev 는 존재하지 않는다.
         * 데이터가 존재한다면 첫번째 데이터를 기준으로 작성한다.
         */
        fun <T> prev(data: List<T>, queries: List<String>, withUrl: Boolean = true): String? {
            if (data.isEmpty()) {
                return null
            }

            return getQueries(data[0], queries, "prev", withUrl)
        }

        fun <T> getQueries(target: T, queries: List<String>, direction: String, withUrl: Boolean = true): String? {
            val cursor: Cursor = Cursor()

            val members = target!!::class.declaredMembers

            members.forEach {
                if (!queries.contains(it.name)) {
                    return@forEach
                }

                it.also {
                    it.isAccessible = true
                }.let {
                    it as KProperty1<in Any, *>
                }.let {
                    cursor.append(it.name, it.getter(target).toString())
                }
            }

            cursor.append("direction", direction)
            return cursor.build(withUrl = withUrl)
        }

        fun <T> build(data: List<T>, queries: List<String>, pageSize: Long, withUrl: Boolean = true): CursorList<T> {
            return CursorList.of(
                data = data,
                prev = prev(data, queries, withUrl),
                next = next(data, queries, pageSize, withUrl),
                pageSize = pageSize
            )
        }
    }
}