package com.openphonics

import com.openphonics.application.response.FlagResponse
import com.openphonics.application.response.LanguageResponse
import com.openphonics.application.response.WordResponse
import com.openphonics.tests.FlagTests
import com.openphonics.tests.LanguageTests
import com.openphonics.tests.WordTests
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
        const val INVALID_FLAG_ID = ";;"
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
    fun testCreateFlag() = test(){client->
        bad(FlagTests.create(FlagTests.invalidFlagRequestLongFlag, client))
        bad(FlagTests.create(FlagTests.invalidFlagRequestNumericFlag, client))
        val id = FlagTests.create(client)
        ok(FlagTests.get(id, client))
    }
    @Test
    fun testUpdateFlag() = test(){client->
        val id = FlagTests.create(client)
        bad(FlagTests.update(null, id, client))
        bad(FlagTests.update(FlagTests.updateFlagRequest, INVALID_FLAG_ID, client))
        FlagTests.update(FlagTests.updateFlagRequest, id, client)
        val response = FlagTests.get(id, client)
        ok(response)
        val data = extractResponse<FlagResponse>(response).flag[0]
        assertEquals(FlagTests.updateFlagRequest.flag, data.flag)
    }
    @Test
    fun testDeleteFlag() = test(){client->
        val id = FlagTests.create(client)
        ok(FlagTests.delete(id, client))
        not(FlagTests.get(id, client))
    }
    @Test
    fun testAllFlag() = test(){client->
        ok(FlagTests.all(client))
    }
    @Test
    fun testCreateLanguage() = test(){client->
        val flag = FlagTests.create(client)
        bad(LanguageTests.create(LanguageTests.invalidLanguageRequestLongNative(flag), client))
        bad(LanguageTests.create(LanguageTests.invalidLanguageRequestLongNative(flag), client))
        bad(LanguageTests.create(LanguageTests.invalidLanguageRequestNumericLanguage(flag), client))
        bad(LanguageTests.create(LanguageTests.invalidLanguageRequestLongLanguage(flag), client))
        bad(LanguageTests.create(LanguageTests.invalidLanguageRequestNumericName(flag), client))
        bad(LanguageTests.create(LanguageTests.invalidLanguageRequestLongName(flag), client))
        bad(LanguageTests.create(LanguageTests.invalidLanguageRequestNumericNative(flag), client))
        bad(LanguageTests.create(LanguageTests.validLanguageRequest(INVALID_FLAG_ID), client))
        val id = LanguageTests.create(client, flag)
        ok(LanguageTests.get(id, client))
    }
    @Test
    fun testUpdateLanguage() = test() { client ->
        val flag = FlagTests.create(client)
        val id = LanguageTests.create(client, flag)
        bad(LanguageTests.update(null, id, client))
        val original = LanguageTests.validUpdateLanguageRequest(flag)
        bad(LanguageTests.update(original, INVALID_INT_ID, client))
        val update = LanguageTests.validUpdateLanguageRequest(flag)
        ok(LanguageTests.update(update, id, client))
        val response = LanguageTests.get(id, client)
        ok(response)
        val data = extractResponse<LanguageResponse>(response).language[0]
        assertEquals(update.nativeId, data.nativeId)
        assertEquals(original.languageId, data.languageId)
        assertEquals(update.languageName, data.languageName)
        assertEquals(update.flag, data.flag.id)
    }
    @Test
    fun testDeleteLanguage() = test() { client->
        val flag = FlagTests.create(client)
        val id = LanguageTests.create(client, flag)
        ok(LanguageTests.delete(id, client))
        not(LanguageTests.get(id, client))
    }
    @Test
    fun testAllLanguages() = test(){client ->
        ok(LanguageTests.all("in", client))
    }
    @Test
    fun testCreateWord() = test() {client->
        val flag = FlagTests.create(client)
        val language = LanguageTests.create(client, flag)
        bad(WordTests.create(null, client))
        bad(WordTests.create(WordTests.invalidWordRequestInvalidPhonic(language), client))
        bad(WordTests.create(WordTests.invalidWordRequestInvalidWord(language), client))
        bad(WordTests.create(WordTests.invalidWordRequestInvalidTranslatedWord(language), client))
        bad(WordTests.create(WordTests.validWordRequest(INVALID_INT_ID), client))
        val id = WordTests.create(client, language)
        ok(WordTests.get(id, client))
    }

    @Test
    fun testUpdateWord() = test() {client->
        val flag = FlagTests.create(client)
        val id = LanguageTests.create(client, flag)
        bad(WordTests.update(null, id, client))
        bad(WordTests.update(WordTests.updateWordRequest, INVALID_INT_ID, client))
        ok(WordTests.update(WordTests.updateWordRequestNoChange, id, client))
        val responseNotUpdated = WordTests.get(id, client)
        ok(responseNotUpdated)
        val dataNotUpdated = extractResponse<WordResponse>(responseNotUpdated).word[0]
        assertEquals(WordTests.validWordRequest(id).phonic, dataNotUpdated.phonic)
        assertEquals(WordTests.validWordRequest(id).sound, dataNotUpdated.sound)
        assertEquals(WordTests.validWordRequest(id).translatedSound, dataNotUpdated.translatedSound)
        assertEquals(WordTests.validWordRequest(id).translatedWord, dataNotUpdated.translatedWord)
        assertEquals(WordTests.validWordRequest(id).word, dataNotUpdated.word)
        ok(WordTests.update(WordTests.updateWordRequest, id, client))
        val response = WordTests.get(id, client)
        ok(response)
        val data = extractResponse<WordResponse>(response).word[0]
        assertEquals(WordTests.updateWordRequest.phonic, data.phonic)
        assertEquals(WordTests.updateWordRequest.sound, data.sound)
        assertEquals(WordTests.updateWordRequest.translatedSound, data.translatedSound)
        assertEquals(WordTests.updateWordRequest.translatedWord, data.translatedWord)
        assertEquals(WordTests.updateWordRequest.word, data.word)
    }
    @Test
    fun testDeleteWord() = test() { client->
        val flag = FlagTests.create(client)
        val language = LanguageTests.create(client, flag)
        val id = WordTests.create(client, language)
        ok(WordTests.delete(id, client))
        not(WordTests.get(id, client))
    }
    @Test
    fun testAllWords() = test(){client ->
        bad(WordTests.all(INVALID_INT_ID, client))
        val flag = FlagTests.create(client)
        val language = LanguageTests.create(client, flag)
        ok(WordTests.all(language, client))
    }
}