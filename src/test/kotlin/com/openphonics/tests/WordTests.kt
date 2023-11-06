package com.openphonics.tests

import com.openphonics.application.request.WordRequest
import com.openphonics.application.request.UpdateWordRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

object WordTests {
    val create: suspend (WordRequest?, HttpClient) -> HttpResponse = { data, client ->
        client.put("/word") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }
    val get: suspend (Int, HttpClient) -> HttpResponse = { id, client ->
        client.get("/word/${id}") {
            contentType(ContentType.Application.Json)
        }
    }
    val update: suspend (UpdateWordRequest?, Int, HttpClient) -> HttpResponse = { data, id, client ->
        client.post("/word/${id}") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }
    val delete: suspend (Int, HttpClient) -> HttpResponse = { id, client ->
        client.delete("/word/${id}") {
            contentType(ContentType.Application.Json)
        }
    }
    val all: suspend (Int, HttpClient) -> HttpResponse = { language, client ->
        client.get("/word/${language}") {
            contentType(ContentType.Application.Json)
        }
    }
}