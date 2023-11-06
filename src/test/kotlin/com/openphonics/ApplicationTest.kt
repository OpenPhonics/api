package com.openphonics

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import kotlin.test.assertEquals

class ApplicationTest {
    companion object {
        @OptIn(KtorExperimentalAPI::class)
        val test: (suspend (HttpClient) -> Unit) -> Unit = { testing ->
            testApplication {
                environment {
                    config = MapApplicationConfig(
                        "key.admin" to "cf6b502cc6d73d9d0b915fad33e249b3c5960ab57225e8179861bf946eb53dee",
                        "key.secret" to "2gkF75yMsUDcZ0yQxIIak0be2TNUk4o0",
                        "database.host" to sqlContainer.host,
                        "database.port" to sqlContainer.firstMappedPort.toString(),
                        "database.name" to sqlContainer.databaseName,
                        "database.user" to sqlContainer.username,
                        "database.maxPoolSize" to "3",
                        "database.driver" to sqlContainer.driverClassName,
                        "database.password" to sqlContainer.password
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

        suspend fun unauth(response: HttpResponse, message: String? = null) {
            assertEquals(HttpStatusCode.Unauthorized, response.status, message ?: response.body())
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