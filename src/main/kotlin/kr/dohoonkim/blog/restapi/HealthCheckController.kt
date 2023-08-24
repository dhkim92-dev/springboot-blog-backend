package kr.dohoonkim.blog.restapi

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.ApiResult.Companion.Ok
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

@RestController
@RequestMapping("api/v1/")
@Tag(name = "Health Check API")
class HealthCheckController {
    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping("health-check")
    fun timestamp() : ResponseEntity<ApiResult<Long>> {
        return Ok(ResultCode.HEALTH_CHECK_SUCCESS, System.currentTimeMillis())
    }
}