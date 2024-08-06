package kr.dohoonkim.blog.restapi.units.common.error

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Path
import kotlinx.coroutines.joinAll
import kr.dohoonkim.blog.restapi.common.error.ErrorCodes
import kr.dohoonkim.blog.restapi.common.error.ErrorResponse
import kr.dohoonkim.blog.restapi.common.error.GlobalExceptionHandler
import kr.dohoonkim.blog.restapi.common.error.exceptions.BusinessException
import kr.dohoonkim.blog.restapi.common.error.exceptions.NotFoundException
import kr.dohoonkim.blog.restapi.support.TestDto
import kr.dohoonkim.blog.restapi.support.TestDummyClass
import org.springframework.core.MethodParameter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestCookieException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import java.lang.IllegalArgumentException
import java.lang.reflect.Method

class GeneralExceptionHandlerTest: AnnotationSpec() {

    private val handler = GlobalExceptionHandler()

    @Test
    fun `BusinessException를 처리하여 ErrorResponse를 반환한다`() {
        val e = BusinessException(HttpStatus.BAD_REQUEST, "G-001", "잘못된 요청")
        val result = handler.businessExceptionHandler(e)

        (result is ResponseEntity) shouldBe  true
        (result.body is ErrorResponse) shouldBe true
        (result.body?.code) shouldBe "G-001"
        (result.body?.message) shouldBe "잘못된 요청"
        (result.body?.status) shouldBe 400
    }

    @Test
    fun `BusinessException을 상속한 에러도 처리된다`() {
        val e = NotFoundException(ErrorCodes.MEMBER_NOT_FOUND)
        val result = handler.businessExceptionHandler(e)

        (result is ResponseEntity) shouldBe  true
        (result.body is ErrorResponse) shouldBe true
        (result.body?.code) shouldBe ErrorCodes.MEMBER_NOT_FOUND.code
        (result.body?.message) shouldBe ErrorCodes.MEMBER_NOT_FOUND.message
        (result.body?.status) shouldBe ErrorCodes.MEMBER_NOT_FOUND.status.value()
    }

    @Test
    fun `MethodArgumentTypeMismatchException 도 처리한다`() {
        val method: Method = TestDummyClass::class.java.getMethod("exampleMethod", String::class.java)
        val methodParameter = MethodParameter(method, 0)
        val e = MethodArgumentTypeMismatchException(
            "value",
            String::class.java,
            "value",
            methodParameter,
            IllegalArgumentException("타입이 맞지 않음")
        )
        val result = handler.methodArgumentTypeMismatchExceptionHandler(e)

        (result is ResponseEntity) shouldBe  true
        (result.body is ErrorResponse) shouldBe true
        result.body?.message shouldBe "잘못된 입력값입니다."
        result.body?.errors?.size shouldNotBe 0
    }

    @Test
    fun `MethodArgumentNotValidException 도 처리한다`() {
        val method: Method = TestDummyClass::class.java.getMethod("dtoMethod", TestDto::class.java)
        val methodParameter = MethodParameter(method, 0)
        val target = TestDto(name="")
        val bindingResult = BeanPropertyBindingResult(target, "dto")
        bindingResult.addError(FieldError("dto","name", "Name should not be blank"))
        val e = MethodArgumentNotValidException(methodParameter, bindingResult as BindingResult)
        val result = handler.methodArgumentNotValidExceptionHandler(e)
        (result is ResponseEntity) shouldBe  true
        (result.body is ErrorResponse) shouldBe true
        result.body?.message shouldBe "잘못된 입력값입니다."
        result.body?.errors?.get(0)?.field  shouldBe "name"
        result.body?.errors?.get(0)?.value  shouldBe ""
        result.body?.errors?.get(0)?.reason  shouldBe "Name should not be blank"
    }

    @Test
    fun `ConstraintViolationExceptionHandler도 처리한다`() {
        val violation = mockk<ConstraintViolation<*>>()
        val path = mockk<Path>()

        every {violation.message} returns "must be greater than 1"
        every { path.toString() } returns "value"
        every { violation.getPropertyPath() } returns path
        every { violation.invalidValue } returns "invalidValue"

        val violations = setOf(violation)
        val exception = ConstraintViolationException(violations)
        val result =handler.constraintViolationExceptionHandler(exception)

        (result is ResponseEntity) shouldBe  true
        (result.body is ErrorResponse) shouldBe true

        val error = result.body?.errors?.get(0)
        error shouldNotBe null
        error?.reason shouldBe "must be greater than 1"
        error?.field shouldBe "value"
        error?.value shouldBe  "invalidValue"
    }

    @Test
    fun `ServletRequestPartException도 처리된다`() {
        val exception  = MissingServletRequestPartException("file")
        val result = handler.missingServletRequestPartExceptionHandler(exception)

        (result is ResponseEntity) shouldBe  true
        (result.body is ErrorResponse) shouldBe true
        result.body?.message shouldBe "file is missing."
    }

    @Test
    fun `ServletRequestParameterException도 처리된다`() {
        val e = MissingServletRequestParameterException("name", "String")
        val result = handler.missingServletRequestParameterException(e)

        (result is ResponseEntity) shouldBe  true
        (result.body is ErrorResponse) shouldBe true
        (result.body?.message) shouldBe "name is missing."
    }

    @Test
    fun `Cookie에 값이 존재하지 않을 때도 처리된다`() {
        val cookieName = "refreshToken"
        val methodParameter = mockk<MethodParameter>(relaxed = true)

        val e = MissingRequestCookieException(cookieName, methodParameter, false)
        val result = handler.missingRequestCookieExceptionHandler(e)

        (result is ResponseEntity) shouldBe  true
        (result.body is ErrorResponse) shouldBe true
        (result.body?.message) shouldBe "refreshToken is not in cookie."
    }

    @Test
    fun `지원하지 않는 Method에 대해서도 처리된다`() {
        val e = HttpRequestMethodNotSupportedException("GET")
        val result = handler.httpRequestMethodNotSupportedException(e)

        (result is ResponseEntity) shouldBe  true
        (result.body is ErrorResponse) shouldBe true
        val error = result.body?.errors?.get(0)

        error shouldNotBe null
        error?.reason shouldBe ErrorCodes.METHOD_NOT_ALLOWED.message
        error?.field shouldBe "http method"
        error?.value shouldBe e.method
    }

    @Test
    fun `HttpMessageNotReadableException 도 처리된다`() {
        val e = mockk<HttpMessageNotReadableException>()
        val result = handler.httpMessageNotReadableExceptionHandler(e)

        (result is ResponseEntity) shouldBe  true
        (result.body is ErrorResponse) shouldBe true
        result.body?.message shouldBe ErrorCodes.HTTP_MESSAGE_NOT_READABLE.message
    }

    @Test
    fun `정의되지 않은 모든 에러를 INTERNAL_SERVER_ERROR로 처리한다`() {
        val errorMessage = "Unknown Exception"
        val e = Exception(errorMessage)
        val result = handler.exceptionHandler(e)
        (result is ResponseEntity) shouldBe  true
        (result.body is ErrorResponse) shouldBe true
        result.body?.message shouldBe errorMessage
    }

    @AfterEach
    fun clean() {
        clearAllMocks()
    }
}
