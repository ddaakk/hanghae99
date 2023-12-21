package kr.hanghae.deploy

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.names.TestName
import io.kotest.core.spec.style.scopes.DescribeSpecContainerScope
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode

class KotestProjectConfig : AbstractProjectConfig() {
    override fun extensions() = listOf(SpringTestContextModeExtension())
}

suspend fun DescribeSpecContainerScope.txContext(name: String, test: suspend DescribeSpecContainerScope.() -> Unit) {
    registerContainer(TestName("TxContexts: ", name, false), false, null) { DescribeSpecContainerScope(this).test() }
}
