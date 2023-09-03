package com.openphonics.route

import dagger.Lazy
import com.openphonics.auth.principal.UserPrincipal
import com.openphonics.controller.ProgressController
import com.openphonics.exception.FailureMessages
import com.openphonics.exception.UnauthorizedActivityException
import com.openphonics.model.request.DepthRequest
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
fun Route.ProgressApi(progressController: Lazy<ProgressController> = controllers.progressController()) {
    authenticate {
        get(ROUTE(PROGRESS)) {
            // Gets all the progress information for each language the user is learning
            val principal = call.principal<UserPrincipal>()
                ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
            val depth = runCatching { call.receive<DepthRequest>() }.getOrElse {
                DepthRequest(Language.LANGUAGE)
            }
            val languageProgressResponse = progressController.get().getAllProgressByUser(principal.user, depth)
            val response = generateHttpResponse(languageProgressResponse)
            call.respond(response.code, response.body)
        }
    }
}

//private fun Route.getAllProgress(controller: Lazy<ProgressController>){
////    get {
//        // Gets all the progress information for each language the user is learning
//        val principal = call.principal<UserPrincipal>()
//            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
//        val depth = runCatching { call.receive<DepthRequest>() }.getOrElse {
//            DepthRequest()
//        }
//        val languageProgressResponse = controller.get().getAllProgressByUser(principal.user, depth)
//        val response = generateHttpResponse(languageProgressResponse)
//        call.respond(response.code, response.body)
////    }
//}