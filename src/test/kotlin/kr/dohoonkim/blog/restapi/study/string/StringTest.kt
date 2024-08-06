package kr.dohoonkim.blog.restapi.study.string

import io.kotest.core.spec.style.AnnotationSpec


class StringTest: AnnotationSpec() {

    @Test
    fun `문자열 split`() {
        val delimiter ="\\."
        val testString = "ab.cd.ef..gg"
        val result = testString.split(delimiter)
        for(c in result) {
            println(c)
        }
    }
}