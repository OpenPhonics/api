package com.openphonics

import com.openphonics.common.core.DataResponse
import com.openphonics.common.core.IdResponse
import com.openphonics.language.LanguageBase
import com.openphonics.tests.LanguageTests
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    companion object {
        const val INVALID_INT_ID = -1
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
    @Test
    fun testCreateLanguage() = test(){client->
        bad(LanguageTests.create(LanguageTests.invalidLanguageRequestLongNative, client))
        bad(LanguageTests.create(LanguageTests.invalidLanguageRequestLongNative, client))
        bad(LanguageTests.create(LanguageTests.invalidLanguageRequestNumericLanguage, client))
        bad(LanguageTests.create(LanguageTests.invalidLanguageRequestLongLanguage, client))
        bad(LanguageTests.create(LanguageTests.invalidLanguageRequestNumericName, client))
        bad(LanguageTests.create(LanguageTests.invalidLanguageRequestLongName, client))
        bad(LanguageTests.create(LanguageTests.invalidLanguageRequestNumericNative, client))
        val id = extractResponse<IdResponse>(LanguageTests.create(LanguageTests.validLanguageRequest, client)).id!!
        val response = LanguageTests.get(id, client)
        ok(response)
        val data = extractResponse<DataResponse<LanguageBase>>(response).data[0]
        ok(LanguageTests.getLanguageByCode(data.languageCode, client))
        ok(LanguageTests.getLanguageByName(data.languageName, client))

    }
    @Test
    fun testUpdateLanguage() = test() { client ->
        val id = extractResponse<IdResponse>(LanguageTests.create(LanguageTests.validLanguageRequest, client)).id!!
        bad(LanguageTests.update(null, id, client))
        not(LanguageTests.update(LanguageTests.validUpdateLanguageRequest, INVALID_INT_ID, client))
        ok(LanguageTests.update(LanguageTests.validUpdateLanguageRequest, id, client))
        val response = LanguageTests.get(id, client)
        ok(response)
        val data = extractResponse<DataResponse<LanguageBase>>(response).data[0]
        assertEquals(LanguageTests.validUpdateLanguageRequest.languageCode, data.languageCode)
        assertEquals(LanguageTests.validUpdateLanguageRequest.countryCode, data.countryCode)
        assertEquals(LanguageTests.validUpdateLanguageRequest.languageName, data.languageName)
    }
    @Test
    fun testDeleteLanguage() = test() { client->
        val id = extractResponse<IdResponse>(LanguageTests.create(LanguageTests.validLanguageRequest, client)).id!!
        ok(LanguageTests.delete(id, client))
        not(LanguageTests.get(id, client))
    }
}