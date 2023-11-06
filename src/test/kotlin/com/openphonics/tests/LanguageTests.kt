package com.openphonics.tests

import com.openphonics.application.request.LanguageRequest
import com.openphonics.application.request.UpdateLanguageRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

object LanguageTests {
    val create: suspend (LanguageRequest?, HttpClient) -> HttpResponse = { data, client ->
        client.put("/language") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }
    val get: suspend (Int, HttpClient) -> HttpResponse = { id, client ->
        client.get("/language/${id}") {
            contentType(ContentType.Application.Json)
        }
    }
    val update: suspend (UpdateLanguageRequest?, Int, HttpClient) -> HttpResponse = { data, id, client ->
        client.post("/language/${id}") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }
    val delete: suspend (Int, HttpClient) -> HttpResponse = { id, client ->
        client.delete("/language/${id}") {
            contentType(ContentType.Application.Json)
        }
    }
    val all: suspend (String, HttpClient) -> HttpResponse = { native, client ->
        client.get("/language/${native}") {
            contentType(ContentType.Application.Json)
        }
    }
}