package kr.hanghae.deploy.component

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import kr.hanghae.deploy.annotation.QueryStringArgResolver
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import java.lang.RuntimeException

@Component
class QueryStringArgumentResolver(
    private val objectMapper: ObjectMapper
) : HandlerMethodArgumentResolver {
    override fun supportsParameter(methodParameter: MethodParameter): Boolean {
        return methodParameter.getParameterAnnotation(QueryStringArgResolver::class.java) != null
    }

    override fun resolveArgument(
        methodParameter: MethodParameter,
        modelAndViewContainer: ModelAndViewContainer?,
        nativeWebRequest: NativeWebRequest,
        webDataBinderFactory: WebDataBinderFactory?
    ): Any? {
        val request = nativeWebRequest.nativeRequest as HttpServletRequest

        if (request == null || request.queryString == null) {
            throw RuntimeException("쿼리 스트링이 필요합니다.")
        }

        val json = qs2json(request.queryString)
        return objectMapper.readValue(json, methodParameter.parameterType)
    }

    private fun qs2json(a: String): String {
        var res = "{\""

        for (i in a.indices) {
            when {
                a[i] == '=' -> res += "\"" + ":" + "\""
                a[i] == '&' -> res += "\"" + "," + "\""
                else -> res += a[i]
            }
        }
        res += "\"" + "}"
        return res
    }
}
