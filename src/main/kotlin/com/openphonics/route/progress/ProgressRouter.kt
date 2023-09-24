package com.openphonics.route.progress

import com.openphonics.auth.principal.UserPrincipal
import com.openphonics.controller.ProgressController
import com.openphonics.data.model.data.Language
import com.openphonics.exception.BadRequestException
import com.openphonics.exception.FailureMessages
import com.openphonics.exception.UnauthorizedActivityException
import com.openphonics.model.request.*
import com.openphonics.model.response.generateHttpResponse
import com.openphonics.plugin.controllers
import dagger.Lazy
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*


@OptIn(KtorExperimentalAPI::class)
fun Route.ProgressApi(progressController: Lazy<ProgressController> = controllers.progressController()) {
    authenticate {
        getAllProgress(progressController)
        getCurrentProgress(progressController)
        getProgressById(progressController)
        postProgress(progressController)
        updateProgress(progressController)
        updateStreak(progressController)
        deleteProgress(progressController)
        updateSection(progressController)
    }
}
private fun Route.getAllProgress(controller: Lazy<ProgressController>){
    get<Progress.LanguageProgress> {
        // Gets all the progress information for each language the user is learning
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val depth = runCatching { call.receive<DepthRequest>() }.getOrElse {
            DepthRequest(Language.LANGUAGE)
        }
        val languageProgressResponse = controller.get().getAllProgressByUser(principal.user, depth)
        val response = generateHttpResponse(languageProgressResponse)
        call.respond(response.code, languageProgressResponse)
    }
}
private fun Route.getProgressById(controller: Lazy<ProgressController>){
    get<Progress.LanguageProgress.Id> {language ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val depth = runCatching { call.receive<DepthRequest>() }.getOrElse {
            DepthRequest(Language.LANGUAGE)
        }
        val languageProgressResponse = controller.get().getLanguageProgress(principal.user, language.id, depth)
        val response = generateHttpResponse(languageProgressResponse)
        call.respond(response.code, languageProgressResponse)
    }
}
private fun Route.getCurrentProgress(controller: Lazy<ProgressController>){
    get<Progress.LanguageProgress.Current> {language ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val depth = runCatching { call.receive<DepthRequest>() }.getOrElse {
            DepthRequest(Language.LANGUAGE)
        }
        val languageProgressResponse = controller.get().getCurrentLanguageProgress(principal.user, depth)
        val response = generateHttpResponse(languageProgressResponse)
        call.respond(response.code, languageProgressResponse)
    }
}
private fun Route.postProgress(controller: Lazy<ProgressController>){
    post<Progress.LanguageProgress.Create> {
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val languageProgressRequest = runCatching { call.receive<LanguageProgressRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val languageProgressResponse = controller.get().addLanguageProgress(principal.user,languageProgressRequest)
        val response = generateHttpResponse(languageProgressResponse)
        call.respond(response.code, languageProgressResponse)
    }
}
private fun Route.updateProgress(controller: Lazy<ProgressController>){
    put<Progress.LanguageProgress.Id> {language ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val xpRequest = runCatching { call.receive<XPRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val languageProgressResponse = controller.get().updateLanguageProgress(principal.user,language.id, xpRequest )
        val response = generateHttpResponse(languageProgressResponse)
        call.respond(response.code, languageProgressResponse)
    }
}
private fun Route.updateStreak(controller: Lazy<ProgressController>){
    put<Progress.LanguageProgress.Id.Streak> {language ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val streakRequest = runCatching { call.receive<StreakRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val languageProgressResponse = controller.get().updateStreak(principal.user,language.parent.id, streakRequest )
        val response = generateHttpResponse(languageProgressResponse)
        call.respond(response.code, languageProgressResponse)
    }
}
private fun Route.deleteProgress(controller: Lazy<ProgressController>){
    delete<Progress.LanguageProgress.Id> {language ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val languageProgressResponse = controller.get().deleteLanguageProgress(principal.user, language.id)
        val response = generateHttpResponse(languageProgressResponse)
        call.respond(response.code, languageProgressResponse)
    }
}
private fun Route.updateSection(controller: Lazy<ProgressController>){
    put<Progress.SectionProgress.Id> {section ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val sectionProgress = runCatching { call.receive<SectionProgressRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val sectionProgressResponse = controller.get().updateSectionProgress(principal.user, section.id, sectionProgress)
        val response = generateHttpResponse(sectionProgressResponse)
        call.respond(response.code, sectionProgressResponse)
    }
}
