package kr.dohoonkim.blog.restapi.common.utility

import org.slf4j.LoggerFactory
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

class Cursor {
    val log = LoggerFactory.getLogger(javaClass)
    val queries: MutableMap<String, String> = mutableMapOf()

    fun append(key: String, value: String): Cursor {
        this.queries[key] = value;
        return this
    }

    fun append(key: String, value: LocalDateTime): Cursor {
        this.append(key, value.format(DateTimeFormatter.ISO_DATE_TIME))
        return this
    }

    fun append(key: String, c: Char): Cursor {
        this.append(key, String(charArrayOf(c)))
        return this
    }

    fun append(key: String, id: UUID): Cursor {
        this.append(key, id.toString())
        return this
    }

    fun append(key: String, value: Int): Cursor {
        this.append(key, value.toString())
        return this
    }

    fun append(key: String, value: Long): Cursor {
        this.append(key, value.toString())
        return this
    }

    fun build(withUrl: Boolean = true): String? {
        val builder = StringBuilder()

        if (withUrl) {
            val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
            var url = request.requestURL.toString()
            if (url.startsWith("http://") && url.indexOf("localhost") < 0) {
                url = url.replace("http://", "https://")
            }

            builder.append(url)
        }

        if (queries.isEmpty()) return null

        builder.append("?")

        queries.forEach { (k, v) ->
            builder.append(k)
            builder.append("=")
            builder.append(v)
            builder.append("&")
        }

        return builder.substring(0, builder.length - 1)
    }

    fun buildQueries(): String? {
        val builder = StringBuilder()
        if (queries.isEmpty()) return null

        builder.append("?")

        queries.forEach { (k, v) ->
            builder.append(k)
            builder.append("=")
            builder.append(v)
            builder.append("&")
        }

        return builder.substring(0, builder.length - 1)
    }

    companion object {
        fun prev(): String {
            return (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request.requestURI
        }
    }
}