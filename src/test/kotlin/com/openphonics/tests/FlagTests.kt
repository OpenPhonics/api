package com.openphonics.tests

import com.openphonics.application.request.FlagRequest
import com.openphonics.application.request.UpdateFlagRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*


object FlagTests {
    val create: suspend (FlagRequest?, HttpClient) -> HttpResponse = { data, client ->
        client.put("/flag") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }
    val get: suspend (String, HttpClient) -> HttpResponse = { id, client ->
        client.get("/flag/${id}") {
            contentType(ContentType.Application.Json)
        }
    }
    val update: suspend (UpdateFlagRequest?, String, HttpClient) -> HttpResponse = { data, id, client ->
        client.post("/flag/${id}") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }
    val delete: suspend (String, HttpClient) -> HttpResponse = { id, client ->
        client.delete("/flag/${id}") {
            contentType(ContentType.Application.Json)
        }
    }
    val all: suspend (HttpClient) -> HttpResponse = { client ->
        client.get("/flag") {
            contentType(ContentType.Application.Json)
        }
    }
}