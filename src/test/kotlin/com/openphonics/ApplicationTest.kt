package com.openphonics

import com.openphonics.application.route.Flag
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.resources.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import kotlin.test.assertEquals
import kotlin.text.get

class ApplicationTest {
    companion object {
        val test: (suspend (HttpClient) -> Unit) -> Unit = { testing ->
            testApplication {
                environment {
                    config = MapApplicationConfig(
                        "db.host" to sqlContainer.host,
                        "db.port" to sqlContainer.firstMappedPort.toString(),
                        "db.name" to sqlContainer.databaseName,
                        "db.user" to sqlContainer.username,
                        "db.max_pool_size" to "3",
                        "db.driver" to sqlContainer.driverClassName,
                        "db.password" to sqlContainer.password
                    )
                }
                application {
                    module()
                }
                val client = createClient {
                    install(ContentNegotiation) {
                        json(
                            json = Json {
                                prettyPrint = true
                            },
                            contentType = ContentType.Application.Json
                        )
                    }
                }
                testing(client)
            }
        }

        suspend inline fun <reified T> extractResponse(response: HttpResponse): T {
            return response.body<T>()
        }

        suspend fun bad(response: HttpResponse, message: String? = null) {
            assertEquals(HttpStatusCode.BadRequest, response.status, message ?: response.body())
        }

        suspend fun ok(response: HttpResponse, message: String? = null) {
            assertEquals(HttpStatusCode.OK, response.status, message ?: response.body())
        }

        suspend fun not(response: HttpResponse, message: String? = null) {
            assertEquals(HttpStatusCode.NotFound, response.status, message ?: response.body())
        }

        private val sqlContainer = PostgreSQLContainer(DockerImageName.parse("postgres:13-alpine"))

        @JvmStatic
        @BeforeClass
        fun setUp() {
            sqlContainer.start()
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            sqlContainer.stop()
        }
    }

}