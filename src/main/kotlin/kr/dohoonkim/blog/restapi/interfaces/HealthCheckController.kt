package kr.dohoonkim.blog.restapi.interfaces

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.CursorList
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import kr.dohoonkim.blog.restapi.common.utility.CookieUtils
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Health Check API")
@ApiResponse(content = [Content(schema = Schema(implementation = ApiResult::class))])
class HealthCheckController {
    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("health-check")
    @ApplicationCode(ResultCode.HEALTH_CHECK_SUCCESS)
    @ApiResponse(content = [Content(schema = Schema(implementation = CursorList::class))])
    fun timestamp(httpServletResponse: HttpServletResponse) : Long {
        val data = mapOf<String, Long>("timestamp" to System.currentTimeMillis())
        CookieUtils.setCookie(httpServletResponse, "test-cookie", CookieUtils.serialize(data) , 180)
        return System.currentTimeMillis()
    }
}