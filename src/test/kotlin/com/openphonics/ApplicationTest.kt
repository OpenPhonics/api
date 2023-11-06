package com.openphonics

import io.kotest.core.spec.style.AnnotationSpec
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.ktor.util.*
import org.testcontainers.containers.PostgreSQLContainer
import java.util.*

@Suppress("unused")
@KtorExperimentalAPI
class ApplicationTest : AnnotationSpec() {

    private val sqlContainer = DatabaseContainer()

    @BeforeClass
    fun setup() {
        sqlContainer.start()
    }
    fun testApp(test: TestApplicationEngine.() -> Unit) {
        withTestApplication(
            {
                (environment.config as MapApplicationConfig).apply {
                    // Set here the properties
                    put("key.secret", UUID.randomUUID().toString())
                    put("db.host", sqlContainer.host)
                    put("db.port", sqlContainer.firstMappedPort.toString())
                    put("db.name", sqlContainer.databaseName)
                    put("db.user", sqlContainer.username)
                    put("db.max_pool_size", "3")
                    put("db.driver", sqlContainer.driverClassName)
                    put("db.password", sqlContainer.password)
                }
                module()
            },
            test
        )
    }

    @AfterClass
    fun cleanup() {
        sqlContainer.stop()
    }
    inner class DatabaseContainer : PostgreSQLContainer<DatabaseContainer>()

}
