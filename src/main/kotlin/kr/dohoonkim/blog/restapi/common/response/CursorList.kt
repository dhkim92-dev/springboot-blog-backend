package kr.dohoonkim.blog.restapi.common.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema

/**
 * Cursor 방식 List 반환 Wrapper
 * @property count 리스트 데이터 크기
 * @property data 실제 데이터
 * @property next 다음 커서 url
 */
@Schema(description = "Cursor 방식 List Wrapper")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CursorList<T>(
    @Schema(description = "반환 데이터 개수", example = "2")
    val count: Int,
    @Schema(description = "실제 반환 데이터 리스트", example = "[1, 2]")
    val data: List<T>,
    @Schema(description = "다음 데이터 조회 URL", example = "https://www.dohoon-kim.kr/articles?created_at=2023-08-10T00:00:00.000Z")
    var next: String?
) {
    companion object {
        fun <T> of(data: List<T>, next: String?, pageSize: Long)
                : CursorList<T> {
            if (data.isEmpty()) {
                return CursorList<T>(0, emptyList(), null)
            }

            var count: Int = if (pageSize + 1 <= data.size) pageSize.toInt() else data.size.toInt()

            return CursorList<T>(count, data.subList(0, count), next)
        }
    }
}
