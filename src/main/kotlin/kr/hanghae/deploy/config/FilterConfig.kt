package kr.hanghae.deploy.config

import kr.hanghae.deploy.filter.AuthFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

//@Configuration
class FilterConfig(
    private val authFilter: AuthFilter,
) {

    @Bean
    fun authFilterRegistrationBean(): FilterRegistrationBean<AuthFilter> {
        val registrationBean = FilterRegistrationBean(authFilter)
        registrationBean.order = 0
        registrationBean.urlPatterns = mutableListOf("/api/v1/seat", "/api/v1/user/charge", "/api/v1/pay")
        return registrationBean
    }
}
