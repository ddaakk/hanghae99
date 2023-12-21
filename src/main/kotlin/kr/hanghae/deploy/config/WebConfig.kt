package kr.hanghae.deploy.config

import kr.hanghae.deploy.component.QueryStringArgumentResolver
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebConfig(
    private val argumentResolver: QueryStringArgumentResolver,
): WebMvcConfigurer {
    override fun addArgumentResolvers(
        argumentResolvers: MutableList<HandlerMethodArgumentResolver?>
    ) {
        argumentResolvers.add(argumentResolver)
    }
}
