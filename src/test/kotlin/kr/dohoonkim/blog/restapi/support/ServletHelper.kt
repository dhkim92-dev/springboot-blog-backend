package kr.dohoonkim.blog.restapi.support

import io.mockk.every
import io.mockk.mockk
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import org.springframework.http.HttpStatus
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.InputStreamReader
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.mock.web.MockHttpServletRequest


fun createMockHttpServletRequest(
    serverName: String= "localhost",
    serverPort: Int = 80,
    scheme: String = "http"
): MockHttpServletRequest {
    val request = MockHttpServletRequest()
    request.serverName = serverName
    request.serverPort = serverPort
    request.scheme = scheme

    return request
}

fun mockServletHttpRequest(): ServerHttpRequest {
    return mockk<ServerHttpRequest>()
}

fun mockServletHttpResponse(code: HttpStatus): ServerHttpResponse {
    val response = mockk<ServerHttpResponse>()
    every {response.setStatusCode(code)} returns Unit
    return response
}

class DelegatingServletInputStream(private val sourceStream: InputStream) : ServletInputStream() {
    override fun read(): Int = sourceStream.read()
    override fun isFinished(): Boolean = sourceStream.available() <= 0
    override fun isReady(): Boolean = true
    override fun setReadListener(readListener: ReadListener) {}
}

class CustomHttpServletRequestWrapper(request: HttpServletRequest, private val newBody: String) : HttpServletRequestWrapper(request) {

    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(newBody.toByteArray())
        return DelegatingServletInputStream(byteArrayInputStream)
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(getInputStream()))
    }
}