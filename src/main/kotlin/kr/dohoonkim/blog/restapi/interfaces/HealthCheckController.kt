package kr.dohoonkim.blog.restapi.interfaces

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.CursorList
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/")
@Tag(name = "Health Check API")
@ApiResponse(content = [Content(schema = Schema(implementation = ApiResult::class))])
class HealthCheckController {
    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("health-check")
    @ApplicationCode(ResultCode.HEALTH_CHECK_SUCCESS)
    @ApiResponse(content = [Content(schema = Schema(implementation = CursorList::class))])
    fun timestamp() : Long {
        return System.currentTimeMillis()
    }
}