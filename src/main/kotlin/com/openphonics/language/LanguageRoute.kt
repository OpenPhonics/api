package com.openphonics.language

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

@Resource(Routes.LANGUAGES)
class Language(){
    @Resource(Routes.ID)
    class Id(val parent: Language = Language(),  val id: Int)
    @Resource(Routes.CODE)
    class Code(val parent: Language = Language()){
        @Resource(Routes.ID)
        class Id(val parent: Code = Code(),  val id: String)
    }
    @Resource(Routes.NAME)
    class Name(val parent: Language = Language()){
        @Resource(Routes.ID)
        class Id(val parent: Name = Name(),  val id: String)
    }

}

@KtorExperimentalAPI
fun Route.LanguageAPI(languageController: Lazy<LanguageController> = controllers.languageController()) {
    put(languageController)
    get(languageController)
    delete(languageController)
    post(languageController)
    all(languageController)
}
private fun Route.put(controller: Lazy<LanguageController>) {
    put<Language> {
        val request = runCatching { call.receive<LanguageCreate>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_LANGUAGE_DETAILS)
        }
        val response = controller.get().create(request)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.all(controller: Lazy<LanguageController>) {
    get<Language> {
        val response = controller.get().all()
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.get(controller: Lazy<LanguageController>) {
    get<Language.Id> {param ->
        val response = controller.get().get(param.id)
        call.respond(generateHttpCode(response), response)
    }
    get<Language.Code.Id> {param ->
        val response = controller.get().getByLanguageCode(param.id)
        call.respond(generateHttpCode(response), response)
    }
    get<Language.Name.Id> {param ->
        val response = controller.get().getByLanguageName(param.id)
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
        val request = runCatching { call.receive<LanguageUpdate>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_LANGUAGE_DETAILS)
        }
        val response = controller.get().update(param.id, request)
        call.respond(generateHttpCode(response), response)
    }
}