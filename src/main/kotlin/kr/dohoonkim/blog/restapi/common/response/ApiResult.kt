package kr.dohoonkim.blog.restapi.common.response

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

class ApiResult<T>(
    val status : Int,
    val code : String,
    val data : T,
    val message : String
) {

  companion object {
      private val _headers = hashMapOf<String, String>("Content-Type" to "application/json;charset=utf-8")
      private val headers = HttpHeaders(LinkedMultiValueMap<String, String>().apply{setAll(_headers)})

      fun <T> Ok(resultCode : ResultCode, data : T) : ResponseEntity<ApiResult<T>> {
          return ResponseEntity(
                  ApiResult(resultCode.status.value(), resultCode.code, data, resultCode.message),
                  headers,
                  resultCode.status)
      }
  }
}