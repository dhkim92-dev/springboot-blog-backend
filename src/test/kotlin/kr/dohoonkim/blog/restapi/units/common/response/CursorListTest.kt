package kr.dohoonkim.blog.restapi.units.common.response

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.common.response.CursorList

internal class CursorListTest: AnnotationSpec() {

    lateinit var testData: List<Int>

    @BeforeEach
    fun setUp() {
        testData = List<Int>(10){it}
    }

    @Test
    fun `pageSize보다 큰 List가 입력으로 주어지면, pageSize만큼만 데이터가 입력된다`() {
        val pageSize = 4L
        val cursorList = CursorList.of(testData,null, pageSize)
        cursorList.count shouldBe pageSize
        cursorList.data.forEachIndexed{ index, data -> data shouldBe testData[index] }
        cursorList.next shouldBe null
    }

    @Test
    fun `pageSize보다 큰 List가 입력으로 주어지면 입력 데이터의 크기와 데이터만 입력된다`() {
        val pageSize = 100L
        val cursorList = CursorList.of(testData,null, pageSize)
        cursorList.count shouldBe testData.size
        cursorList.data.forEachIndexed{ index, data -> data shouldBe testData[index] }
        cursorList.next shouldBe null
    }
}