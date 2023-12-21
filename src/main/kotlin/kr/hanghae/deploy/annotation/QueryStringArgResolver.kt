package kr.hanghae.deploy.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.RetentionPolicy


@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class QueryStringArgResolver
