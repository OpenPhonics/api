package com.openphonics
import com.openphonics.Testing.VALID_ADMIN_CLASS_CODE
import com.openphonics.Testing.VALID_CLASS_CODE
import com.openphonics.model.request.AdminSignUpRequest
import com.openphonics.model.response.AuthResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals
import com.openphonics.route.auth.Routing as Auth


object Testing {
    const val VALID_NATIVE_ID = "en"
    const val INVALID_NUMERIC_NATIVE_ID = "e4"
    const val INVALID_LONG_NATIVE_ID = "enen"

    const val VALID_LANGUAGE_ID = "ta"
    const val INVALID_NUMERIC_LANGUAGE_ID = "e4"
    const val INVALID_LONG_LANGUAGE_ID = "enen"

    const val VALID_LANGUAGE_NAME = "Tamil"
    const val INVALID_NUMERIC_LANGUAGE_NAME = "T4s"
    const val INVALID_LONG_LANGUAGE_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

    const val VALID_TITLE = "Basic Words"
    const val INVALID_NUMERIC_TITLE = "B4 Wo4rs"
    const val INVALID_LONG_TITLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

    const val VALID_ORDER = 0
    const val INVALID_ORDER = -1

    const val VALID_LESSON_COUNT = 2
    const val INVALID_LESSON_COUNT = -1

    const val VALID_FLAG = "in"
    const val INVALID_LONG_FLAG = "ins"
    const val INVALID_NUMERIC_FLAG = "p4"

    const val VALID_FLAG_IMG = "https://raw.githubusercontent.com/catamphetamine/country-flag-icons/master/3x2/IN.svg"

    const val VALID_CLASS_CODE = "eg3a1r"
    const val VALID_ADMIN_CLASS_CODE = "e5fe1k"
    const val BLANK_CLASS_CODE = ""

    const val VALID_CLASS_NAME = "Bharathi Primary"
    const val INVALID_LONG_CLASS_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    const val INVALID_NUMERIC_CLASS_NAME = "A4"

    const val VALID_NAME = "Advait Vedant"
    const val INVALID_BLANK_NAME = ""
    const val INVALID_SHORT_NAME = "a b"
    const val INVALID_LONG_NAME = "AdvaitAdvait SurajUnnikrishnans"
    const val INVALID_NUMERIC_NAME = "Abd4 hello"
    const val INVALID_NO_SPACE_NAME = "Advit"

    const val VALID_PHONIC = "həˈləʊ"
    const val INVALID_PHONIC = "hte4t"
    const val VALID_WORD = "hello"
    const val INVALID_WORD= "he11o"
    const val VALID_TRANSLATED_WORD = "வணக்கம்"
    const val INVALID_TRANSLATED_WORD = "வணக4்கம்"
    const val VALID_SOUND = "https://api.dictionaryapi.dev/media/pronunciations/en/hello-au.mp3"
    const val VALID_TRANSLATED_SOUND = "https://api.dictionaryapi.dev/media/pronunciations/en/hello-au.mp3"
}

class ApplicationTest {
    companion object {
        val test: (suspend (HttpClient)->Unit) -> Unit = { testing->
            testApplication {
                val client = createClient {
                    install(ContentNegotiation) {
                        json()
                    }
                }
                testing(client)
            }
        }
        suspend inline fun <reified T> extractResponse(response: HttpResponse): T
        {
            return response.body<T>()
        }
        const val REGISTER_URL = "${Auth.AUTH}/${Auth.REGISTER}/${Auth.ADMIN}"
        val registerAdmin: suspend (AdminSignUpRequest?, HttpClient) -> HttpResponse = { data, client ->
            client.post(REGISTER_URL) {
                contentType(ContentType.Application.Json)
                setBody(data)
            }
        }
    }
    @Test
    fun s() = test() { client ->
//        assertEquals(5,4)
       val response = registerAdmin(
            AdminSignUpRequest(
            "Advait Veden",
            VALID_ADMIN_CLASS_CODE
        ),
            client
        )
//        assertEquals("", extractResponse<AuthResponse>(response).message)
        assertEquals(HttpStatusCode.OK,response.status)
    }



//    companion object {
////        private val sqlContainer = DatabaseContainer()
////        class DatabaseContainer : PostgreSQLContainer<DatabaseContainer>()
//
//        private val sqlContainer = PostgreSQLContainer(DockerImageName.parse("postgres:13-alpine"))
//            .withDatabaseName("notykt_dev_db")
//            .withUsername("postgres")
//            .withPassword("postgres")
//
//        @JvmStatic
//        @BeforeClass
//        fun setUp() {
//            sqlContainer.start()
//        }
//
//        @JvmStatic
//        @AfterClass
//        fun tearDown() {
//            sqlContainer.stop()
//        }
//    }
}