package com.openphonics.application.route

import com.openphonics.application.controller.FlagController
import com.openphonics.application.exception.FailureMessages
import com.openphonics.application.request.FlagRequest
import com.openphonics.application.request.UpdateFlagRequest
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

@Resource(Routes.FLAGS)
class Flag(){
    @Resource(Routes.ID)
    class Id(val parent: Flag = Flag(),  val id: String)
    @Resource(Routes.ALL)
    class All(val parent: Flag = Flag())
}
@KtorExperimentalAPI
fun Route.FlagAPI(flagController: Lazy<FlagController> = controllers.flagController()) {
    put(flagController)
    get(flagController)
    delete(flagController)
    post(flagController)
    all(flagController)
}
private fun Route.all(controller: Lazy<FlagController>) {
    get<Flag> {
        val response = controller.get().all()
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.put(controller: Lazy<FlagController>) {
    put<Flag> {
        val request = runCatching { call.receive<FlagRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_FLAG_DETAILS)
        }
        val response = controller.get().create(request)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.get(controller: Lazy<FlagController>) {
    get<Flag.Id> {param ->
        val response = controller.get().get(param.id)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.delete(controller: Lazy<FlagController>) {
    delete<Flag.Id> {param ->
        val response = controller.get().delete(param.id)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.post(controller: Lazy<FlagController>) {
    post<Flag.Id> {param ->
        val request = runCatching { call.receive<UpdateFlagRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_UPDATE_FLAG_DETAILS)
        }
        val response = controller.get().update(param.id, request)
        call.respond(generateHttpCode(response), response)
    }
}


