package kr.dohoonkim.blog.restapi.units.common.response.pagination

import com.nimbusds.jose.shaded.gson.internal.bind.util.ISO8601Utils
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import kr.dohoonkim.blog.restapi.common.response.pagination.CursorBuilder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

internal class CursorBuilderTest: AnnotationSpec() {

    private lateinit var cursorBuilder : CursorBuilder

    @BeforeEach
    fun setUp() {
        cursorBuilder = CursorBuilder()
    }

    @Test
    fun `커서에 String 값을 추가하고 build를 하면 URI가 반환된다`() {
        val cursor = cursorBuilder.append("name", "Charles" as Any).build()
        cursor shouldBe "?name=Charles"
    }

    @Test
    fun `base url을 입력하면 build 시 URL 반환된다`() {
        val cursor = cursorBuilder.append("name", "Charles" as Any).build("http://localhost")
        cursor shouldBe "http://localhost?name=Charles"
    }

    @Test
    fun `커서에 값을 추가하고 추가 여부를 확인하면 true를 반환한다`() {
        cursorBuilder.append("name", "Charles")
        cursorBuilder.contains("name") shouldBe true
    }

    @Test
    fun `커서에 String 값을 추가하고 추가된 값을 확인할 수 있다`() {
        cursorBuilder.append("name", "Charles" as Any)
        cursorBuilder.get("name") shouldBe "Charles"
    }

    @Test
    fun `커서에 Char 값을 추가하고 추가된 값을 확인할 수 있다`() {
        cursorBuilder.append("value", 'C' as Any)
        cursorBuilder.get("value") shouldBe "C"
    }

    @Test
    fun `커서에 Long 값을 추가하고 추가된 값을 확인할 수 있다`() {
        cursorBuilder.append("value", 1L as Any)
        cursorBuilder.get("value") shouldBe "1"
    }

    @Test
    fun `커서에 Int 값을 추가하고 추가된 값을 확인할 수 있다`() {
        cursorBuilder.append("value", 1 as Any)
        cursorBuilder.get("value") shouldBe "1"
    }

    @Test
    fun `커서에 LocalDatetime 값을 추가하면 ISO_8601 포맷으로 변환된다`() {
        val dt = LocalDateTime.now()
        cursorBuilder.append("value", dt as Any)
        cursorBuilder.get("value") shouldBe dt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @Test
    fun `커서에 UUID 값을 추가하면 추가된다`() {
        val value = UUID.randomUUID()
        cursorBuilder.append("value", value as Any)
        cursorBuilder.get("value") shouldBe value.toString()
    }

    @Test
    fun `커서에 지원하지 않는 데이터 타입을 입력하면 추가하면 추가되지 않는다`() {
        val data = mapOf("zero" to "one")
        cursorBuilder.append("value", data as Any)
        cursorBuilder.get("value") shouldBe null
    }
}

