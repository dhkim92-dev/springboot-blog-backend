package kr.dohoonkim.blog.restapi.study

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.board.dto.CategorySummaryDto

class ObjectMapperTest : AnnotationSpec(){
    private val objectMapper = ObjectMapper().registerModule(kotlinModule())

    @Test
    fun `JSON과 Object로 서로 변환된다`() {
        val data = CategorySummaryDto(id = 1L, name = "test")
        val jsonData = objectMapper.writeValueAsString(data)
        println(jsonData)
        val categoryDto = objectMapper.readValue(jsonData, CategorySummaryDto::class.java)

        data.id shouldBe categoryDto.id
        data.name shouldBe categoryDto.name
    }

}