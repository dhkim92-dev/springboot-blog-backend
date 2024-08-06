package kr.dohoonkim.blog.restapi.common.response.pagination

import org.slf4j.LoggerFactory
import org.springframework.cglib.core.Local
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class CursorBuilder {

    private val log = LoggerFactory.getLogger(javaClass)
    private val queries: MutableMap<String, String> = mutableMapOf()

    fun contains(key: String): Boolean {
        return queries.contains(key)
    }

    operator fun get(key: String): Any? {
        return queries[key]
    }

    fun append(key:String, value: Any): CursorBuilder {
        return when(value) {
            is String -> append(key, value as String)
            is Int -> append(key, value as Int)
            is Long -> append(key, value as Long)
            is LocalDateTime -> append(key, value as LocalDateTime)
            is Char -> append(key, value as Char)
            is UUID -> append(key, value as UUID)
            else -> this
        }
    }

    fun append(key: String, value: String): CursorBuilder {
        this.queries[key] = value;
        return this
    }

    fun append(key: String, value: LocalDateTime): CursorBuilder {
        this.append(key, value.format(DateTimeFormatter.ISO_DATE_TIME))
        return this
    }

    fun append(key: String, c: Char): CursorBuilder {
        this.append(key, String(charArrayOf(c)))
        return this
    }

    fun append(key: String, id: UUID): CursorBuilder {
        this.append(key, id.toString())
        return this
    }

    fun append(key: String, value: Int): CursorBuilder {
        this.append(key, value.toString())
        return this
    }

    fun append(key: String, value: Long): CursorBuilder {
        this.append(key, value.toString())
        return this
    }

    fun build(url: String? = null): String? {
        val builder = StringBuilder()

        url?.let{
            builder.append(url)
        }

        if (queries.isEmpty()) return null

        buildQueries()?.let {
            builder.append(it)
        }

        return builder.substring(0, builder.length)
    }

    private fun buildQueries(): String? {
        val builder = StringBuilder()
        builder.append("?")
        queries.forEach { (k, v) ->
            builder.append("$k=$v&")
        }
        return builder.substring(0, builder.length - 1)
    }
}