package com.openphonics.tests

import com.openphonics.ApplicationTest
import com.openphonics.ApplicationTest.Companion.extractResponse
import com.openphonics.ApplicationTest.Companion.ok
import com.openphonics.application.request.FlagRequest
import com.openphonics.application.request.UpdateFlagRequest
import com.openphonics.application.response.StrResponse
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


object FlagTests {
    const val VALID_FLAG = "in"
    const val UPDATED_FLAG = "us"
    const val INVALID_LONG_FLAG = "ins"
    const val INVALID_NUMERIC_FLAG = "p4"
    const val VALID_FLAG_IMG = "https://raw.githubusercontent.com/catamphetamine/country-flag-icons/master/3x2/IN.svg"
    const val UPDATED_FLAG_IMG = "https://raw.githubusercontent.com/catamphetamine/country-flag-icons/master/3x2/US.svg"
    val validFlagRequest = FlagRequest(
        VALID_FLAG_IMG,
        VALID_FLAG
    )
    val updateFlagRequest = UpdateFlagRequest(
        UPDATED_FLAG_IMG
    )
    val invalidFlagRequestLongFlag = FlagRequest(
        VALID_FLAG_IMG,
        INVALID_LONG_FLAG
    )
    val invalidFlagRequestNumericFlag = FlagRequest(
        VALID_FLAG_IMG,
        INVALID_NUMERIC_FLAG
    )
    // Define a suspend function for creating a flag
    suspend fun create(data: FlagRequest?, client: HttpClient): HttpResponse {
        return client.put("/flag") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }

    // Define a suspend function for getting a flag by ID
    suspend fun get(id: String, client: HttpClient): HttpResponse {
        return client.get("/flag/$id") {
            contentType(ContentType.Application.Json)
        }
    }

    // Define a suspend function for updating a flag by ID
    suspend fun update(data: UpdateFlagRequest?, id: String, client: HttpClient): HttpResponse {
        return client.post("/flag/$id") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }

    // Define a suspend function for deleting a flag by ID
    suspend fun delete(id: String, client: HttpClient): HttpResponse {
        return client.delete("/flag/$id") {
            contentType(ContentType.Application.Json)
        }
    }

    // Define a suspend function for fetching all flags
    suspend fun all(client: HttpClient): HttpResponse {
        return client.get("/flag") {
            contentType(ContentType.Application.Json)
        }
    }
    suspend fun create(client: HttpClient): String {
        val response = create(validFlagRequest, client)
        ok(response)
        return extractResponse<StrResponse>(response).id!!
    }
}