package kr.hanghae.deploy

import io.kotest.core.listeners.AfterTestListener
import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.JdbcTemplate
import org.junit.jupiter.api.AfterEach
import org.springframework.boot.test.context.SpringBootTest

class DatabaseCleanUpExecutor (
    private val jdbcTemplate: JdbcTemplate
) : TestListener {

    override suspend fun afterTest(testCase: TestCase, result: TestResult) {
        val truncateQueries = getTruncateQueries(jdbcTemplate)
        truncateTables(jdbcTemplate, truncateQueries)
    }

    private fun getTruncateQueries(jdbcTemplate: JdbcTemplate): List<String> {
        return jdbcTemplate.queryForList("SELECT Concat('TRUNCATE TABLE `', TABLE_NAME, '`;') AS q " +
            "FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'", String::class.java)
    }

    private fun truncateTables(jdbcTemplate: JdbcTemplate, truncateQueries: List<String>) {
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY FALSE")
        truncateQueries.forEach { v -> execute(jdbcTemplate, v) }
        execute(jdbcTemplate, "SET REFERENTIAL_INTEGRITY TRUE")
    }

    private fun execute(jdbcTemplate: JdbcTemplate, query: String) {
        jdbcTemplate.execute(query)
    }
}
