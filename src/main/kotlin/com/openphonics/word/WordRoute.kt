package com.openphonics.word

import com.openphonics.common.Routes
import com.openphonics.common.exception.FailureMessages
import com.openphonics.common.plugins.controllers
import com.openphonics.common.utils.generateHttpCode
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
    @Resource(Routes.ID)
    class Id(val parent: Word = Word(),  val id: Int)
    @Resource(Routes.LANGUAGE)
    class Language(val parent: Word = Word()){
        @Resource(Routes.ID)
        class Id(val parent: Language = Language(),  val id: Int)
    }
}

@KtorExperimentalAPI
fun Route.WordAPI(wordController: Lazy<WordController> = controllers.wordController()) {
    put(wordController)
    get(wordController)
    delete(wordController)
    post(wordController)
    all(wordController)
}
private fun Route.put(controller: Lazy<WordController>) {
    put<Word> {
        val request = runCatching { call.receive<WordCreate>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_LANGUAGE_DETAILS)
        }
        val response = controller.get().create(request)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.all(controller: Lazy<WordController>) {
    get<Word.Language.Id> {param ->
        val response = controller.get().all(param.id)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.get(controller: Lazy<WordController>) {
    get<Word.Id> {param ->
        val response = controller.get().get(param.id)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.delete(controller: Lazy<WordController>) {
    delete<Word.Id> {param ->
        val response = controller.get().delete(param.id)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.post(controller: Lazy<WordController>) {
    post<Word.Id> {param ->
        val request = runCatching { call.receive<WordUpdate>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_LANGUAGE_DETAILS)
        }
        val response = controller.get().update(param.id, request)
        call.respond(generateHttpCode(response), response)
    }
}