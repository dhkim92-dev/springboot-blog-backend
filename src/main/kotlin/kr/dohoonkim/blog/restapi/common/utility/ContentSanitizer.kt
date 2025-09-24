package kr.dohoonkim.blog.restapi.common.utility

import org.owasp.html.HtmlPolicyBuilder
import org.owasp.html.PolicyFactory
import org.springframework.stereotype.Component

@Component
class ContentSanitizer {

    private val commentPolicy: PolicyFactory = HtmlPolicyBuilder()
        // 기본 텍스트 서식만 허용
        .allowElements("strong", "em", "code")
        .allowElements("p", "br")
        .toFactory()

    /**
     * 댓글 내용을 안전하게 정화
     * HTTPS 악성 스크립트도 차단됨
     */
    fun sanitize(content: String): String {
        if (content.isBlank()) return ""

        // 1차: OWASP HTML Sanitizer로 기본 정화
        val sanitized = commentPolicy.sanitize(content.trim())

        // 2차: 추가 보안 검증 (HTTPS 악성 링크 포함)
        return validateAndCleanContent(sanitized)
    }

    private fun validateAndCleanContent(content: String): String {
        var cleaned = content

        // 의심스러운 HTTPS 링크 패턴 제거
        val suspiciousPatterns = listOf(
            // 단축 URL 또는 의심스러운 도메인
            "https://bit\\.ly/[^\\s\"'<>]+".toRegex(),
            "https://tinyurl\\.com/[^\\s\"'<>]+".toRegex(),
            "https://[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}[^\\s\"'<>]*".toRegex(), // IP 주소

            // 의심스러운 파라미터
            "https://[^\\s\"'<>]*[?&](redirect|url|return|callback)=[^\\s\"'<>]*".toRegex(),

            // 파일 다운로드 링크
            "https://[^\\s\"'<>]*\\.(exe|bat|scr|com|pif|cmd|vbs|js)".toRegex(RegexOption.IGNORE_CASE)
        )

        suspiciousPatterns.forEach { pattern ->
            cleaned = cleaned.replace(pattern) { matchResult ->
                "[링크 제거됨: 보안상 위험]"
            }
        }

        return cleaned
    }
}