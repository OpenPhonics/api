package com.openphonics.tests

import com.openphonics.ApplicationTest
import com.openphonics.ApplicationTest.Companion.extractResponse
import com.openphonics.ApplicationTest.Companion.ok
import com.openphonics.application.request.LanguageRequest
import com.openphonics.application.request.UpdateLanguageRequest
import com.openphonics.application.response.IntResponse
import com.openphonics.application.response.StrResponse
import com.openphonics.application.route.Routes
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
object LanguageTests {
    const val VALID_NATIVE_ID = "en"
    const val UPDATED_NATIVE_ID = "en"
    const val INVALID_NUMERIC_NATIVE_ID = "e4"
    const val INVALID_LONG_NATIVE_ID = "enen"

    const val VALID_LANGUAGE_ID = "ta"
    const val UPDATED_LANGUAGE_ID = "ra"
    const val INVALID_NUMERIC_LANGUAGE_ID = "e4"
    const val INVALID_LONG_LANGUAGE_ID = "enen"

    const val VALID_LANGUAGE_NAME = "Tamil"
    const val UPDATED_LANGUAGE_NAME = "Engra"
    const val INVALID_NUMERIC_LANGUAGE_NAME = "T4s"
    const val INVALID_LONG_LANGUAGE_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

    val languageRequest: (String, String, String) -> (String) -> LanguageRequest = { native, language, name ->
        {flag ->
            LanguageRequest(
                native,
                language,
                name,
                flag
            )
        }
    }
    val updateLanguageRequest: (String?, String?, String?) -> (String?) -> UpdateLanguageRequest = { native, language, name ->
        {flag ->
            UpdateLanguageRequest(
                native,
                language,
                name,
                flag
            )
        }
    }
    val validLanguageRequest = languageRequest(
        VALID_NATIVE_ID,
        VALID_LANGUAGE_ID,
        VALID_LANGUAGE_NAME
    )
    val validUpdateLanguageRequest = updateLanguageRequest(
        UPDATED_NATIVE_ID,
        null,
        UPDATED_LANGUAGE_NAME
    )
    val invalidLanguageRequestNumericNative = languageRequest(
        INVALID_NUMERIC_NATIVE_ID,
        VALID_LANGUAGE_ID,
        VALID_LANGUAGE_NAME
    )
    val invalidLanguageRequestLongNative = languageRequest(
        INVALID_LONG_NATIVE_ID,
        VALID_LANGUAGE_ID,
        VALID_LANGUAGE_NAME
    )
    val invalidLanguageRequestNumericLanguage = languageRequest(
        VALID_NATIVE_ID,
        INVALID_NUMERIC_LANGUAGE_ID,
        VALID_LANGUAGE_NAME
    )
    val invalidLanguageRequestLongLanguage = languageRequest(
        VALID_NATIVE_ID,
        INVALID_LONG_LANGUAGE_ID,
        VALID_LANGUAGE_NAME
    )
    val invalidLanguageRequestNumericName = languageRequest(
        VALID_NATIVE_ID,
        VALID_LANGUAGE_ID,
        INVALID_NUMERIC_LANGUAGE_NAME
    )
    val invalidLanguageRequestLongName = languageRequest(
        VALID_NATIVE_ID,
        VALID_LANGUAGE_ID,
        INVALID_LONG_LANGUAGE_NAME
    )
    // Define a suspend function for creating a language
    suspend fun create(data: LanguageRequest?, client: HttpClient): HttpResponse {
        return client.put(Routes.LANGUAGES) {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }

    // Define a suspend function for getting a language by ID
    suspend fun get(id: Int, client: HttpClient): HttpResponse {
        return client.get("${Routes.LANGUAGES}/$id") {
            contentType(ContentType.Application.Json)
        }
    }

    // Define a suspend function for updating a language by ID
    suspend fun update(data: UpdateLanguageRequest?, id: Int, client: HttpClient): HttpResponse {
        return client.post("${Routes.LANGUAGES}/$id") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }

    // Define a suspend function for deleting a language by ID
    suspend fun delete(id: Int, client: HttpClient): HttpResponse {
        return client.delete("${Routes.LANGUAGES}/$id") {
            contentType(ContentType.Application.Json)
        }
    }

    // Define a suspend function for fetching all languages by native language name
    suspend fun all(native: String, client: HttpClient): HttpResponse {
        return client.get("${Routes.LANGUAGES}/${Routes.ALL}/$native") {
            contentType(ContentType.Application.Json)
        }
    }
    suspend fun create(client: HttpClient, flag: String): Int {
        val response = create(validLanguageRequest(flag), client)
        ok(response)
        return extractResponse<IntResponse>(response).id!!
    }
}
