package kr.dohoonkim.blog.restapi.study

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.kotlinModule
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.application.board.dto.CategorySummaryDto
import org.objectweb.asm.TypeReference

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

    @Test
    fun `Class 내부의 변수를 가져온다`() {
        val data = Sample(0, "sample1", Producer(id = 3, name = "sample producer"))
        val node = objectMapper.valueToTree<JsonNode>(data)
        println(node.get("producer").get("name").toString())
        node["producer"]["name"].asText() shouldBe "sample producer"
    }

    @Test
    fun `키값을 통해 Class 내부 변수를 가져온다`() {
        val key = mapOf("producer#name" to "producerName")
        val data = Sample(0, "sample1", Producer(id = 3, name = "sample producer"))
        val node = objectMapper.valueToTree<JsonNode>(data)

        val keyMap = key.map { it -> {it.value to it.key.split('#')} }

        keyMap.forEach { entry -> run {
            println("entry first : ${entry().first}")
            println("entry first length : ${entry().first.length}")
            println("entry second first : ${entry().second.first()}")
            println("entry second : ${entry().second}")
        }}

        keyMap.forEach {
            println("search about ${it().first} ${it().second}")
            var targetNode : JsonNode = node
            var found = true
            it().second.forEach {key ->
                if(!targetNode.has(key) ) {
                    println("  key not exists.")
                    found = false
                    return@forEach
                }
                println("  key exists.")
                targetNode = targetNode.get(key)
            }

            if(!found) return@forEach
            println("result : ${it().first} : ${targetNode?.asText()}")
        }
    }

    data class Producer(val id : Long = 0, val name : String) {

    }
    data class Sample(val id : Long, val name : String, val producer : Producer) {

    }
}