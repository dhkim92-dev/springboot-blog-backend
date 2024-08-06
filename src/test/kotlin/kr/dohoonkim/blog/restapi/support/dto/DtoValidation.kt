package kr.dohoonkim.blog.restapi.support.dto

import io.kotest.core.spec.style.BehaviorSpec
import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory

open class DtoValidation: BehaviorSpec() {
    protected val validator = Validation.buildDefaultValidatorFactory()
        .validator

}