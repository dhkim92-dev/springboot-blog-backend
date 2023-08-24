package kr.dohoonkim.blog.restapi

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import java.time.LocalDateTime

@SpringBootTest
class ApplicationTests (
		@Autowired val om : ObjectMapper
){

	@Test
	fun contextLoads() {
	}

	@Test
	fun localDateTimeSerializerTest() {
		val dt = LocalDateTime.now()
		println(om.writeValueAsString(dt))
	}

}
