package kr.dohoonkim.blog.restapi.units.common.response

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.common.response.PageList
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

internal class PageListTest: AnnotationSpec() {

    private lateinit var testData: List<Long>
    private lateinit var pageable: Pageable

    @BeforeEach
    fun setUp() {
        testData = List<Long>(10) {it -> it.toLong() }
        pageable = PageRequest.of(0, 10)
    }

    @Test
    fun `데이터를 입력해주면 PagList 로 반환된다`() {
        val pageList = PageList.of(testData, testData.size.toLong(), pageable)

        pageList.pageNum shouldBe  pageable.pageNumber
        pageList.count shouldBe testData.size
        pageList.pageSize shouldBe pageable.pageSize
        pageList.data.forEachIndexed{ i, v -> v shouldBe testData[i] }
    }
}