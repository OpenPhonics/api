package com.openphonics.tests

import com.openphonics.common.Routes
import com.openphonics.language.LanguageCreate
import com.openphonics.language.LanguageUpdate
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

object LanguageTests {
    const val VALID_LANGUAGE_CODE = "en"
    const val UPDATED_LANGUAGE_CODE = "en"
    const val INVALID_NUMERIC_LANGUAGE_CODE = "e4"
    const val INVALID_LONG_LANGUAGE_CODE = "enen"

    const val VALID_COUNTRY_CODE = "en"
    const val UPDATED_COUNTRY_CODE = "en"
    const val INVALID_NUMERIC_COUNTRY_CODE = "e4"
    const val INVALID_LONG_COUNTRY_CODE = "enen"

    const val VALID_LANGUAGE_NAME = "Tamil"
    const val UPDATED_LANGUAGE_NAME = "Engra"
    const val INVALID_NUMERIC_LANGUAGE_NAME = "T4s"
    const val INVALID_LONG_LANGUAGE_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    val validLanguageRequest = LanguageCreate(
        VALID_LANGUAGE_CODE,
        VALID_LANGUAGE_NAME,
        VALID_COUNTRY_CODE
    )
    val validUpdateLanguageRequest = LanguageUpdate(
        UPDATED_LANGUAGE_CODE,
        UPDATED_LANGUAGE_NAME,
        UPDATED_COUNTRY_CODE

    )
    val invalidLanguageRequestNumericNative = LanguageCreate(
        INVALID_NUMERIC_LANGUAGE_CODE,
        VALID_LANGUAGE_NAME,
        VALID_COUNTRY_CODE,
    )
    val invalidLanguageRequestLongNative = LanguageCreate(
        INVALID_LONG_LANGUAGE_CODE,
        VALID_LANGUAGE_NAME,
        VALID_COUNTRY_CODE
    )
    val invalidLanguageRequestNumericLanguage = LanguageCreate(
        VALID_LANGUAGE_CODE,
        VALID_LANGUAGE_NAME,
        INVALID_NUMERIC_COUNTRY_CODE

    )
    val invalidLanguageRequestLongLanguage = LanguageCreate(
        VALID_LANGUAGE_CODE,
        VALID_LANGUAGE_NAME,
        INVALID_LONG_COUNTRY_CODE
    )
    val invalidLanguageRequestNumericName = LanguageCreate(
        VALID_LANGUAGE_CODE,
        INVALID_NUMERIC_LANGUAGE_NAME,
        VALID_COUNTRY_CODE,
    )
    val invalidLanguageRequestLongName = LanguageCreate(
        VALID_LANGUAGE_CODE,
        INVALID_LONG_LANGUAGE_NAME,
        VALID_COUNTRY_CODE,

    )
    // Define a suspend function for creating a language
    suspend fun create(data: LanguageCreate?, client: HttpClient): HttpResponse {
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

    // Define a suspend function for getting a language by name
    suspend fun getLanguageByName(name: String, client: HttpClient): HttpResponse {
        return client.get("${Routes.LANGUAGES}/${Routes.NAME}/$name"){
            contentType(ContentType.Application.Json)
        }
    }

    // Define a suspend function for getting a language by code
    suspend fun getLanguageByCode(code: String, client: HttpClient): HttpResponse {
        return client.get("${Routes.LANGUAGES}/${Routes.CODE}/$code") {
            contentType(ContentType.Application.Json)
        }
    }


    // Define a suspend function for updating a language by ID
    suspend fun update(data: LanguageUpdate?, id: Int, client: HttpClient): HttpResponse {
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
}
