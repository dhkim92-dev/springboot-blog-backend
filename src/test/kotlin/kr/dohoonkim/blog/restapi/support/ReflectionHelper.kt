package kr.dohoonkim.blog.restapi.support

import io.mockk.every
import io.mockk.mockk
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import kr.dohoonkim.blog.restapi.common.response.ResultCode
import kr.dohoonkim.blog.restapi.common.response.annotation.ApplicationCode
import kr.dohoonkim.blog.restapi.common.response.pagination.Cursor
import kr.dohoonkim.blog.restapi.common.response.pagination.CursorPagination
import kr.dohoonkim.blog.restapi.interfaces.board.dto.PostArticleRequest
import org.springframework.core.MethodParameter


data class TestDto(
    @field: NotEmpty
    val name: String
) {
}
class TestDummyClass {
    fun exampleMethod(param: String) {
        // do nothing
    }

    fun dtoMethod(dto: TestDto) {

    }
}

fun mockCursorMethodParameter(
    name: String,
    key: String,
    inherit : Boolean
): MethodParameter {
    val parameter = mockMethodParameter(name)
    val annotation = mockk<Cursor>()
    every { annotation.key } returns key
    every { annotation.inherit } returns inherit
    every { parameter.getParameterAnnotation(Cursor::class.java) } returns annotation

    return parameter
}

fun mockMethodParameter(name: String) : MethodParameter {
    val parameter = mockk<MethodParameter>()
    every { parameter.parameterName } returns name
    every { parameter.getParameterName() } returns name
    return parameter
}

fun mockApplicationCodeMethodParameter(
    code: ResultCode
): MethodParameter {

    val parameter = mockk<MethodParameter>()
    val annotation = mockk<ApplicationCode>()
    every { annotation.code } returns code
    every { parameter.getMethodAnnotation(ApplicationCode::class.java) } returns annotation
    every { parameter.hasMethodAnnotation(ApplicationCode::class.java) } returns true
    return parameter
}

fun mockCursorPaginationMethodParameter()
: MethodParameter {
    val parameter = mockk<MethodParameter>()
    val annotation = mockk<CursorPagination>()
    every { parameter.getMethodAnnotation(CursorPagination::class.java) } returns annotation
    return parameter
}