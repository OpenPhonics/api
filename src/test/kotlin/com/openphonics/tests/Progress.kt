package com.openphonics.tests


import com.openphonics.model.request.*
import com.openphonics.route.progress.Routing
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

object LanguageProgress {
    const val LANGUAGES_PROGRESS_URL = "${Routing.PROGRESS}/${Routing.LANGUAGES}"
    val LANGUAGE_PROGRESS_BY_ID_URL: (String) -> String = {id: String ->"${Routing.PROGRESS}/${Routing.LANGUAGES}/${id}"}
    const val CREATE_LANGUAGE_PROGRESS_URL = "${Routing.PROGRESS}/${Routing.LANGUAGES}/${Routing.CREATE}"
    val LANGUAGE_PROGRESS_STREAK_BY_ID_URL: (String) -> String = {id: String ->"${Routing.PROGRESS}/${Routing.LANGUAGES}/${id}/${Routing.STREAK}"}

    val getLanguageProgress: suspend (String, DepthRequest?, HttpClient) -> HttpResponse = { token, depth, client ->
        client.get(LANGUAGES_PROGRESS_URL) {
            contentType(ContentType.Application.Json)
            setBody(depth)
            bearerAuth(token)
        }
    }
    val getLanguageProgressById: suspend (String, String, DepthRequest?, HttpClient) -> HttpResponse = { token, id, depth, client ->
        client.get(LANGUAGE_PROGRESS_BY_ID_URL(id)) {
            contentType(ContentType.Application.Json)
            setBody(depth)
            bearerAuth(token)
        }
    }
    val createLanguageProgress: suspend (String, LanguageProgressRequest?, HttpClient) -> HttpResponse = { token, request, client ->
        client.post(CREATE_LANGUAGE_PROGRESS_URL) {
            contentType(ContentType.Application.Json)
            setBody(request)
            bearerAuth(token)
        }
    }
    val updateLanguageProgress: suspend (String, String, XPRequest?, HttpClient) -> HttpResponse = { token, id, request, client ->
        client.put(LANGUAGE_PROGRESS_BY_ID_URL(id)) {
            contentType(ContentType.Application.Json)
            setBody(request)
            bearerAuth(token)
        }
    }
    val updateStreakLanguageProgress: suspend (String, String, StreakRequest?, HttpClient) -> HttpResponse = { token, id, request, client ->
        client.put(LANGUAGE_PROGRESS_STREAK_BY_ID_URL(id)) {
            contentType(ContentType.Application.Json)
            setBody(request)
            bearerAuth(token)
        }
    }
    val deleteLanguageProgress: suspend (String, String, HttpClient) -> HttpResponse = { token, id, client ->
        client.delete(LANGUAGE_PROGRESS_BY_ID_URL(id)) {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
        }
    }

//    val getLanguageProgress: suspend (String, HttpClient) -> HttpResponse = { token, client ->
//        client.get(LANGUAGES_PROGRESS_URL) {
//            contentType(ContentType.Application.Json)
//            bearerAuth(token)
//        }
//    }
}
object SectionProgress {
    val SECTION_PROGRESS_BY_ID_URL: (String) -> String = {id: String ->"${Routing.PROGRESS}/${Routing.SECTIONS}/${id}"}
    val updateSectionProgress: suspend (String, String, SectionProgressRequest?, HttpClient) -> HttpResponse = { token, id, request, client ->
        client.put(SECTION_PROGRESS_BY_ID_URL(id)) {
            contentType(ContentType.Application.Json)
            setBody(request)
            bearerAuth(token)
        }
    }
}