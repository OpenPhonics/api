package com.openphonics.application.route

import com.openphonics.application.controller.WordController
import com.openphonics.application.exception.FailureMessages
import com.openphonics.application.request.WordRequest
import com.openphonics.application.request.UpdateWordRequest
import com.openphonics.application.response.generateHttpResponse
import com.openphonics.plugins.controllers
import dagger.Lazy
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

@Resource(Routes.WORDS)
class Word(){
    @Resource(Routes.ALL)
    class All(val parent: Word = Word(), val language: Int)
    @Resource(Routes.ID)
    class Id(val parent: Word = Word(),  val id: Int)
}

@KtorExperimentalAPI
fun Route.WordAPI(wordController: Lazy<WordController> = controllers.wordController()) {
    put(wordController)
    get(wordController)
    delete(wordController)
    post(wordController)
    all(wordController)
}
private fun Route.all(controller: Lazy<WordController>) {
    get<Word.All> {param->
        val response = controller.get().all(param.language)
        call.respond(generateHttpResponse(response))
    }
}
private fun Route.put(controller: Lazy<WordController>) {
    put<Word> {
        val request = runCatching { call.receive<WordRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_WORD_DETAILS)
        }
        val response = controller.get().create(request)
        call.respond(generateHttpResponse(response))
    }
}
private fun Route.get(controller: Lazy<WordController>) {
    get<Word.Id> {param ->
        val response = controller.get().get(param.id)
        call.respond(generateHttpResponse(response))
    }
}
private fun Route.delete(controller: Lazy<WordController>) {
    delete<Word.Id> {param ->
        val response = controller.get().delete(param.id)
        call.respond(generateHttpResponse(response))
    }
}
private fun Route.post(controller: Lazy<WordController>) {
    post<Word.Id> {param ->
        val request = runCatching { call.receive<UpdateWordRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_UPDATE_WORD_DETAILS)
        }
        val response = controller.get().update(param.id, request)
        call.respond(generateHttpResponse(response))
    }
}