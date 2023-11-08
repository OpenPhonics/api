package com.openphonics.tests

import com.openphonics.ApplicationTest
import com.openphonics.ApplicationTest.Companion.extractResponse
import com.openphonics.ApplicationTest.Companion.ok
import com.openphonics.application.request.WordRequest
import com.openphonics.application.request.UpdateWordRequest
import com.openphonics.application.response.IntResponse
import com.openphonics.application.response.StrResponse
import com.openphonics.application.route.Routes
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

object WordTests {
    const val VALID_PHONIC = "həˈləʊ"
    const val INVALID_PHONIC = "hte4t"
    const val VALID_WORD = "hello"
    const val UPDATED_WORD = "huh"
    const val INVALID_WORD= "he11o"
    const val VALID_TRANSLATED_WORD = "வணக்கம்"
    const val INVALID_TRANSLATED_WORD = "வணக4்கம்"
    const val VALID_SOUND = "https://api.dictionaryapi.dev/media/pronunciations/en/hello-au.mp3"
    const val VALID_TRANSLATED_SOUND = "https://api.dictionaryapi.dev/media/pronunciations/en/hello-au.mp3"

    val wordRequest: (String, String, String, String, String) -> (Int) -> WordRequest = { phonic, sound, translatedSound, translatedWord, word ->
        {language ->
            WordRequest(
                phonic,
                sound,
                translatedSound,
                translatedWord,
                word,
                language
            )
        }
    }
    val validWordRequest = wordRequest(
        VALID_PHONIC,
        VALID_SOUND,
        VALID_TRANSLATED_SOUND,
        VALID_TRANSLATED_WORD,
        VALID_WORD
    )
    val updateWordRequest = UpdateWordRequest(
        VALID_PHONIC,
        VALID_SOUND,
        VALID_TRANSLATED_SOUND,
        VALID_TRANSLATED_WORD,
        UPDATED_WORD
    )
    val updateWordRequestNoChange = UpdateWordRequest(
        null,
        null,
        null,
        null,
        null
    )
    val invalidWordRequestInvalidPhonic = wordRequest(
        INVALID_PHONIC,
        VALID_SOUND,
        VALID_TRANSLATED_SOUND,
        VALID_TRANSLATED_WORD,
        VALID_WORD
    )
    val invalidWordRequestInvalidWord = wordRequest(
        VALID_PHONIC,
        VALID_SOUND,
        VALID_TRANSLATED_SOUND,
        VALID_TRANSLATED_WORD,
        INVALID_WORD
    )
    val invalidWordRequestInvalidTranslatedWord = wordRequest(
        VALID_PHONIC,
        VALID_SOUND,
        VALID_TRANSLATED_SOUND,
        INVALID_TRANSLATED_WORD,
        VALID_WORD
    )

    suspend fun create(client: HttpClient, language: Int): Int {
        val response = create(validWordRequest(language), client)
        ok(response)
        return extractResponse<IntResponse>(response).id!!
    }

    // Define a suspend function for creating a word
    suspend fun create(data: WordRequest?, client: HttpClient): HttpResponse {
        return client.put(Routes.WORDS) {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }

    // Define a suspend function for getting a word by ID
    suspend fun get(id: Int, client: HttpClient): HttpResponse {
        return client.get("${Routes.WORDS}/$id") {
            contentType(ContentType.Application.Json)
        }
    }

    // Define a suspend function for updating a word by ID
    suspend fun update(data: UpdateWordRequest?, id: Int, client: HttpClient): HttpResponse {
        return client.post("${Routes.WORDS}/$id") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }

    // Define a suspend function for deleting a word by ID
    suspend fun delete(id: Int, client: HttpClient): HttpResponse {
        return client.delete("${Routes.WORDS}/$id") {
            contentType(ContentType.Application.Json)
        }
    }

    // Define a suspend function for fetching all words by language
    suspend fun all(language: Int, client: HttpClient): HttpResponse {
        return client.get("${Routes.WORDS}/${Routes.ALL}/$language") {
            contentType(ContentType.Application.Json)
        }
    }
}