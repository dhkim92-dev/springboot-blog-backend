package kr.dohoonkim.blog.restapi.common.response

import io.swagger.v3.oas.annotations.media.Schema
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

/**
 * Envelop Pattern 적용 Response Wrapper
 * @author dhkim92.dev
 * @since 2023.08.10
 * @property status HttpStatus
 * @property code Application ResultCode
 * @property data 요청에 대한 응답 데이터
 * @property message 요청 처리 결과에 대한 메시지
 */
@Schema(description = "응답 객체 Wrapper")
class ApiResult<T>(
    @Schema(description = "HTTP status code", example = "200")
    val status: Int,
    @Schema(description = "어플리케이션 응답 코드", example = "G001")
    val code: String,
    @Schema(description = "요청 응답 데이터", example = "324812357231")
    val data: T,
    @Schema(description = "처리 결과에 대한 메시지", example = "서버 인스턴스 상태를 체크하였습니다")
    val message: String,
) {

    companion object {
        fun <T> Ok(resultCode: ResultCode, data: T): ApiResult<T> {
            return ApiResult(resultCode.status.value(), resultCode.code, data, resultCode.message)
        }
    }
}