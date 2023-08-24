package kr.dohoonkim.blog.restapi.common.error

import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import jakarta.validation.ConstraintViolationException
import kr.dohoonkim.blog.restapi.common.error.exceptions.BusinessException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingRequestCookieException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.support.MissingServletRequestPartException

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler
    protected fun invalidJwtExceptionHandler(e : JWTVerificationException)
    : ResponseEntity<ErrorResponse>{
        val response = ErrorResponse.of(ErrorCode.AUTHENTICATION_FAIL, e.message!!)
        return ResponseEntity(response, UNAUTHORIZED);
    }

    @ExceptionHandler
    protected fun badCredentialsExceptionHandler(e : BadCredentialsException)
    :ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(ErrorCode.AUTHENTICATION_FAIL)
        return ResponseEntity(response, UNAUTHORIZED)
    }

    @ExceptionHandler
    protected fun businessExceptionHandler(e : BusinessException)
    : ResponseEntity<ErrorResponse> {
        this.log.error("business exception occured : {}, {}", e.message,e.errors)
        return makeResponse(e.errorCode)
    }

    @ExceptionHandler
    protected fun methodArgumentTypeMismatchExceptionHandler(e : MethodArgumentTypeMismatchException)
            : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(e)
        return ResponseEntity(response, BAD_REQUEST)
    }

    @ExceptionHandler
    protected fun methodArgumentNotValidExceptionHandler(e : MethodArgumentNotValidException)
    : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.bindingResult)
        return ResponseEntity(response, BAD_REQUEST);
    }

    @ExceptionHandler
    protected fun constraintViolationExceptionHandler(e : ConstraintViolationException)
    : ResponseEntity<ErrorResponse>{
        val response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.constraintViolations);
        return ResponseEntity(response, BAD_REQUEST)
    }

    @ExceptionHandler
    protected fun missingServletRequestParameterException(e : MissingServletRequestParameterException)
    : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.parameterName)
        return ResponseEntity(response, BAD_REQUEST)
    }

    @ExceptionHandler
    protected fun missingServletRequestPartExceptionHandler(e : MissingServletRequestPartException)
    : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.requestPartName)
        return ResponseEntity(response, BAD_REQUEST)
    }

    @ExceptionHandler
    protected fun missingServletRequestCookieExceptionHandler(e : MissingRequestCookieException)
    : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE, e.cookieName)
        return ResponseEntity(response, BAD_REQUEST)
    }

    @ExceptionHandler
    protected fun httpRequestMethodNotSupportedException(e : HttpRequestMethodNotSupportedException)
    : ResponseEntity<ErrorResponse> {
        val errors : MutableList<ErrorResponse.FieldError> = mutableListOf()
        errors.add(ErrorResponse.FieldError("http method", e.method, ErrorCode.METHOD_NOT_ALLOWED.message))
        val response = ErrorResponse.of(ErrorCode.INVALID_HEADER, errors)
        return ResponseEntity(response, BAD_REQUEST)
    }

    @ExceptionHandler
    protected fun httpMessageNotReadableExceptionHandler(e : HttpMessageNotReadableException)
    : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(ErrorCode.HTTP_MESSAGE_NOT_READABLE)
        return ResponseEntity(response, BAD_REQUEST)
    }

    @ExceptionHandler
    protected fun exceptionHandler(e : Exception)
    : ResponseEntity<ErrorResponse> {
        val response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR)
        return ResponseEntity(response, INTERNAL_SERVER_ERROR)
    }

    private fun makeResponse(errorCode : ErrorCode) : ResponseEntity<ErrorResponse>{
        val response = ErrorResponse.of(errorCode);
        return ResponseEntity(response, errorCode.status)
    }
}