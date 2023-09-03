package com.openphonics.route

import dagger.Lazy
import com.openphonics.auth.principal.UserPrincipal
import com.openphonics.controller.AuthController
import com.openphonics.controller.DataController
import com.openphonics.exception.BadRequestException
import com.openphonics.exception.FailureMessages
import com.openphonics.exception.UnauthorizedActivityException
import com.openphonics.model.request.DepthRequest
import com.openphonics.model.request.LanguageRequest
import com.openphonics.model.response.generateHttpResponse
import com.openphonics.plugin.controllers
import com.openphonics.data.model.data.Language
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*

@OptIn(KtorExperimentalAPI::class)
fun Route.DataApi(dataController: Lazy<DataController> = controllers.dataController()) {
    authenticate {
        route(ROUTE(DATA)){
            route(ROUTE(LANGUAGE)){
                postLanguage(dataController)
                getAllLanguages(dataController)
                languageOperations(dataController)
            }
        }
    }
}

private fun Route.postLanguage(controller: Lazy<DataController>){
    post(ROUTE(CREATE)) {
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val language = runCatching { call.receive<LanguageRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val languageResponse = controller.get().addLanguage(principal.user, language)
        val response = generateHttpResponse(languageResponse)
        call.respond(response.code, response.body)
    }
}

private fun Route.getAllLanguages(controller: Lazy<DataController>){
    get {
        // Gets all the progress information for each language the user is learning
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val depth = runCatching { call.receive<DepthRequest>() }.getOrElse {
            DepthRequest(Language.LANGUAGE)
        }
        val languageResponse = controller.get().getAllLanguages(principal.user.native, depth)
        val response = generateHttpResponse(languageResponse)
        call.respond(response.code, response.body)
    }
}
private fun Route.languageOperations(controller: Lazy<DataController>){
    route (ROUTE_VAR(LANGUAGE_ID)){
        getLanguageById(controller)
        updateLanguageById(controller)
        deleteLanguageById(controller)
    }
}
private fun Route.getLanguageById(controller: Lazy<DataController>){
    get {
        val languageId = call.parameters[LANGUAGE_ID]?.toIntOrNull() ?: return@get
        // Gets all the progress information for each language the user is learning
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val depth = runCatching { call.receive<DepthRequest>() }.getOrElse {
            DepthRequest(Language.LANGUAGE)
        }
        val languageResponse = controller.get().getAllLanguages(principal.user.native, depth)
        val response = generateHttpResponse(languageResponse)
        call.respond(response.code, response.body)
    }
}
private fun Route.updateLanguageById(controller: Lazy<DataController>){
    put {

    }
}

private fun Route.deleteLanguageById(controller: Lazy<DataController>){
    delete {

    }
}
