package kr.dohoonkim.blog.restapi.common.error

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.ConstraintViolation
import org.springframework.validation.BindingResult
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException


@Schema(description = "Error 발생 시 응답 wrapper")
class ErrorResponse {
    @Schema(description = "HTTP 응답 코드")
    var status: Int

    @Schema(description = "어플리케이션 코드")
    var code: String

    @Schema(description = "메시지")
    var message: String

    @Schema(description = "에러 발생 필드")
    var errors: List<FieldError>

    private constructor(code: ErrorCode, message: String) {
        this.status = code.status.value()
        this.code = code.code
        this.message = message
        this.errors = listOf()
    }

    private constructor(code: ErrorCode, errors: List<FieldError>) {
        this.status = code.status.value()
        this.code = code.code
        this.message = code.message
        this.errors = errors
    }

    private constructor(code: ErrorCode) {
        this.status = code.status.value()
        this.code = code.code
        this.message = code.message
        this.errors = listOf()
    }

    private constructor(status: Int, message: String, code: String) {
        this.message = message
        this.status = status
        this.code = code;
        this.errors = listOf()
    }

    companion object {

        fun of(code: ErrorCode, bindingResult: BindingResult) = ErrorResponse(code, FieldError.of(bindingResult))

        fun of(code: ErrorCode) = ErrorResponse(code)

        fun of(status: Int, message: String, code: String) = ErrorResponse(status, message, code)

        fun of(code: ErrorCode, violations: Set<ConstraintViolation<*>>) =
            ErrorResponse(code, FieldError.of(violations))

        fun of(code: ErrorCode, errors: List<FieldError>) = ErrorResponse(code, errors)

        fun of(code: ErrorCode, parameterName: String) = ErrorResponse(code, parameterName)

        fun of(e: MethodArgumentTypeMismatchException): ErrorResponse {
            val value = e.value?.toString() ?: ""
            val errors = FieldError.of(e.name, value, e.errorCode);
            return ErrorResponse(ErrorCodes.INVALID_INPUT_VALUE, errors);
        }

    }

    class FieldError(
        val field: String,
        val value: String,
        val reason: String
    ) {
        companion object {
            fun of(field: String, value: String, reason: String): List<FieldError> {
                val fieldErrors: List<FieldError> = listOf(FieldError(field, value, reason))
                return fieldErrors
            }

            fun of(bindingResult: BindingResult): List<FieldError> {
                val fieldErrors: List<org.springframework.validation.FieldError> = bindingResult.fieldErrors

                return fieldErrors.stream().map {
                    FieldError(
                        it.field,
                        it.rejectedValue?.toString() ?: "",
                        it.defaultMessage ?: ""
                    )
                }.toList()
            }

            fun of(constraintViolations: Set<ConstraintViolation<*>>): List<FieldError> {
                return constraintViolations.toList()
                    .map { error ->
                        val invalidValue = error.invalidValue?.toString() ?: ""
                        val index = error.propertyPath.toString().indexOf(".")
                        val propertyPath = error.propertyPath.toString().substring(index + 1)
                        FieldError(propertyPath, invalidValue, error.message)
                    }
            }
        }
    }
}