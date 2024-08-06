package kr.dohoonkim.blog.restapi.common.utility

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

/**
 * RequestContextHolder에서 현재 RequestAttributes들을 가져와 쿼리 스트링을
 * Map으로 변환하여 반환한다.
 */
@Component
class UrlUtility() {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 현재 요청의 URL을 반환한다
     */
    fun getURL(): String {
        val request = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val url = request.requestURL.toString()

        return if(url.startsWith("https:")) {
            url
        } else if(url.startsWith("http:") &&  !(url.contains("localhost") || url.contains("127.0.0.1"))  ) {
            url.replace("http:","https:")
        } else {
            url
        }
    }

    /**
     * 현재 요청의 Query 를 반환한다.
     * @return query map
     */
    fun getQueryParams(): Map<String, String> {
        val requestAttributes = (RequestContextHolder.currentRequestAttributes()) as? ServletRequestAttributes
        val request = requestAttributes?.request
        return request?.parameterMap?.mapValues { entry -> entry.value.joinToString(",") }
            ?: mapOf()
    }
}