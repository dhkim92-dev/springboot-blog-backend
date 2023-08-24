package kr.dohoonkim.blog.restapi.integration.interfaces

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import kr.dohoonkim.blog.restapi.common.error.ErrorCode
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse
import kr.dohoonkim.blog.restapi.common.response.ApiResult
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockHttpServletRequestDsl
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultMatcher
import org.springframework.test.web.servlet.result.ContentResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter


abstract class RestControllerTest {

    @Autowired
    lateinit var objectMapper: ObjectMapper
    lateinit var mockMvc : MockMvc

    @BeforeEach
    internal open fun setUp(webAppContext : WebApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext)
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .build()
    }


    fun MockHttpServletRequestDsl.jsonContent(value : Any) {
        content = objectMapper.writeValueAsString(value)
        contentType = MediaType.APPLICATION_JSON
    }

    fun ContentResultMatchers.success(code : ResultCode, value : Any) : ResultMatcher {
        return json(objectMapper.writeValueAsString(ApiResult.Ok(code, value)), true)
    }

    fun ContentResultMatchers.error(code : ErrorCode, message : String) : ResultMatcher {
        return json(objectMapper.writeValueAsString(ErrorResponse.of(code, message)), true)
    }
}
