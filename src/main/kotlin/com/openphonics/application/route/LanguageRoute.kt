package com.openphonics.application.route

import com.openphonics.application.controller.LanguageController
import com.openphonics.application.exception.FailureMessages
import com.openphonics.application.request.LanguageRequest
import com.openphonics.application.request.UpdateLanguageRequest
import com.openphonics.plugins.controllers
import com.openphonics.utils.generateHttpCode
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

@Resource(Routes.LANGUAGES)
class Language(){
    @Resource(Routes.ALL)
    class All(val parent: Language = Language(), val native: String)
    @Resource(Routes.ID)
    class Id(val parent: Language = Language(),  val id: Int)
}

@KtorExperimentalAPI
fun Route.LanguageAPI(languageController: Lazy<LanguageController> = controllers.languageController()) {
    put(languageController)
    get(languageController)
    delete(languageController)
    post(languageController)
    all(languageController)
}
private fun Route.all(controller: Lazy<LanguageController>) {
    get<Language.All> {param->
        val response = controller.get().all(param.native)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.put(controller: Lazy<LanguageController>) {
    put<Language> {
        val request = runCatching { call.receive<LanguageRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_LANGUAGE_DETAILS)
        }
        val response = controller.get().create(request)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.get(controller: Lazy<LanguageController>) {
    get<Language.Id> {param ->
        val response = controller.get().get(param.id)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.delete(controller: Lazy<LanguageController>) {
    delete<Language.Id> {param ->
        val response = controller.get().delete(param.id)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.post(controller: Lazy<LanguageController>) {
    post<Language.Id> {param ->
        val request = runCatching { call.receive<UpdateLanguageRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_LANGUAGE_DETAILS)
        }
        val response = controller.get().update(param.id, request)
        call.respond(generateHttpCode(response), response)
    }
}