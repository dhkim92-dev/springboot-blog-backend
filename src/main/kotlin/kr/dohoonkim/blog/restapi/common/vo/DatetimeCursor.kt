package kr.dohoonkim.blog.restapi.common.vo

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class DatetimeCursor(
    @JsonProperty("created_at")
    val createdAt: LocalDateTime?
)
