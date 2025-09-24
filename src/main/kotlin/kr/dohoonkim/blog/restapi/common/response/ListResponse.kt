package kr.dohoonkim.blog.restapi.common.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.common.utility.CursorNextUrlBuilder

/**
 * Cursor 방식 List 반환 Wrapper
 * @property count 리스트 데이터 크기
 * @property data 실제 데이터
 */
@Schema(description = "Cursor 방식 List Wrapper")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ListResponse<T>(
    @Schema(description = "반환 데이터 개수", example = "2")
    val count: Int,
    @Schema(description = "실제 반환 데이터 리스트", example = "[1, 2]")
    val items: List<T>,
) : BaseResponse() {

    companion object {

        fun<T> from(data: List<T>): ListResponse<T> {
            return ListResponse(
                count = data.size,
                items = data
            )
        }

        fun<T> of(
            size: Int,
            items: List<T>,
            extractors: Map<String, (T) -> Any>
        ): ListResponse<T> {
            val responseItems = if (items.size > size) {
                items.take(size)
            } else {
                items
            }
            val response = ListResponse(
                count = responseItems.size,
                items = responseItems
            )

            val nextUrl = CursorNextUrlBuilder.buildNextUrlWithExtractor(items, extractors, size)
            response._links["next"] = HateoasLink(nextUrl)

            return response
        }
    }
}
