package com.openphonics.route.data

import com.openphonics.auth.principal.UserPrincipal
import com.openphonics.controller.DataController
import com.openphonics.data.model.data.Language
import com.openphonics.data.model.data.Section
import com.openphonics.data.model.data.Unit
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
fun Route.DataApi(dataController: Lazy<DataController> = controllers.dataController()) {
    authenticate {
        languageOperations(dataController)
        unitOperations(dataController)
        sectionOperations(dataController)
        wordOperations(dataController)
        sentenceOperations(dataController)
        flagOperations(dataController)
    }
}

private fun Route.postLanguage(controller: Lazy<DataController>){
    post<Data.Language.Create>{
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val language = runCatching { call.receive<LanguageRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val languageResponse = controller.get().addLanguage(principal.user, language)
        val response = generateHttpResponse(languageResponse)
        call.respond(response.code, languageResponse)
    }
}
private fun Route.getAllLanguages(controller: Lazy<DataController>){
    get <Data.Language>{
        // Gets all the progress information for each language the user is learning
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val depth = runCatching { call.receive<DepthRequest>() }.getOrElse {
            DepthRequest(Language.LANGUAGE)
        }
        val languageResponse = controller.get().getAllLanguages(principal.user.native, depth)
        val response = generateHttpResponse(languageResponse)
        call.respond(response.code, languageResponse)
    }
}
private fun Route.getLanguageById(controller: Lazy<DataController>){
    get<Data.Language.Id> {language->
        // Gets all the progress information for each language the user is learning
        val depth = runCatching { call.receive<DepthRequest>() }.getOrElse {
            DepthRequest(Language.LANGUAGE)
        }
        val languageResponse = controller.get().getLanguage(language.id, depth)
        val response = generateHttpResponse(languageResponse)
        call.respond(response.code, languageResponse)
    }
}
private fun Route.updateLanguageById(controller: Lazy<DataController>){
    put<Data.Language.Id>{language ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val languageRequest  = runCatching { call.receive<LanguageRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val languageResponse = controller.get().updateLanguage(principal.user, language.id, languageRequest)
        val response = generateHttpResponse(languageResponse)
        call.respond(response.code, languageResponse)
    }
}
private fun Route.deleteLanguageById(controller: Lazy<DataController>){
    delete<Data.Language.Id> {language ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val languageResponse = controller.get().deleteLanguage(principal.user, language.id)
        val response = generateHttpResponse(languageResponse)
        call.respond(response.code, languageResponse)
    }
}
private fun Route.languageOperations(controller: Lazy<DataController>){
    getAllLanguages(controller)
    postLanguage(controller)
    getLanguageById(controller)
    updateLanguageById(controller)
    deleteLanguageById(controller)
}


private fun Route.postUnit(controller: Lazy<DataController>){
    post<Data.Unit.Create>{
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val unit = runCatching { call.receive<UnitRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val unitResponse = controller.get().addUnit(principal.user, unit)
        val response = generateHttpResponse(unitResponse)
        call.respond(response.code, unitResponse)
    }
}
private fun Route.getUnitById(controller: Lazy<DataController>){
    get<Data.Unit.Id> {unit->
        // Gets all the progress information for each unit the user is learning
        val depth = runCatching { call.receive<DepthRequest>() }.getOrElse {
            DepthRequest(Unit.UNIT)
        }
        val unitResponse = controller.get().getUnit(unit.id, depth)
        val response = generateHttpResponse(unitResponse)
        call.respond(response.code, unitResponse)
    }
}
private fun Route.updateUnitById(controller: Lazy<DataController>){
    put<Data.Unit.Id>{unit ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val unitRequest  = runCatching { call.receive<UnitRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val unitResponse = controller.get().updateUnit(principal.user, unit.id, unitRequest)
        val response = generateHttpResponse(unitResponse)
        call.respond(response.code, unitResponse)
    }
}
private fun Route.deleteUnitById(controller: Lazy<DataController>){
    delete<Data.Unit.Id> {unit ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val unitResponse = controller.get().deleteUnit(principal.user, unit.id)
        val response = generateHttpResponse(unitResponse)
        call.respond(response.code, unitResponse)
    }
}
private fun Route.unitOperations(controller: Lazy<DataController>){
    postUnit(controller)
    getUnitById(controller)
    updateUnitById(controller)
    deleteUnitById(controller)
}

private fun Route.postSection(controller: Lazy<DataController>){
    post<Data.Section.Create>{
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val section = runCatching { call.receive<SectionRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val sectionResponse = controller.get().addSection(principal.user, section)
        val response = generateHttpResponse(sectionResponse)
        call.respond(response.code, sectionResponse)
    }
}
private fun Route.getSectionById(controller: Lazy<DataController>){
    get<Data.Section.Id> {section->
        // Gets all the progress information for each section the user is learning
        val depth = runCatching { call.receive<DepthRequest>() }.getOrElse {
            DepthRequest(Section.SECTION)
        }
        val sectionResponse = controller.get().getSection(section.id, depth)
        val response = generateHttpResponse(sectionResponse)
        call.respond(response.code, sectionResponse)
    }
}
private fun Route.updateSectionById(controller: Lazy<DataController>){
    put<Data.Section.Id>{section ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val sectionRequest  = runCatching { call.receive<SectionRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val sectionResponse = controller.get().updateSection(principal.user, section.id, sectionRequest)
        val response = generateHttpResponse(sectionResponse)
        call.respond(response.code, sectionResponse)
    }
}
private fun Route.deleteSectionById(controller: Lazy<DataController>){
    delete<Data.Section.Id> {section ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val sectionResponse = controller.get().deleteSection(principal.user, section.id)
        val response = generateHttpResponse(sectionResponse)
        call.respond(response.code, sectionResponse)
    }
}
private fun Route.sectionOperations(controller: Lazy<DataController>){
    postSection(controller)
    getSectionById(controller)
    updateSectionById(controller)
    deleteSectionById(controller)
}


private fun Route.postWord(controller: Lazy<DataController>){
    post<Data.Word.Create>{
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val word = runCatching { call.receive<WordRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val wordResponse = controller.get().addWord(principal.user, word)
        val response = generateHttpResponse(wordResponse)
        call.respond(response.code, wordResponse)
    }
}
private fun Route.getWordById(controller: Lazy<DataController>){
    get<Data.Word.Id> {word->
        // Gets all the progress information for each word the user is learning
        val wordResponse = controller.get().getWord(word.id)
        val response = generateHttpResponse(wordResponse)
        call.respond(response.code, wordResponse)
    }
}
private fun Route.updateWordById(controller: Lazy<DataController>){
    put<Data.Word.Id>{word ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val wordRequest  = runCatching { call.receive<WordRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val wordResponse = controller.get().updateWord(principal.user, word.id, wordRequest)
        val response = generateHttpResponse(wordResponse)
        call.respond(response.code, wordResponse)
    }
}
private fun Route.deleteWordById(controller: Lazy<DataController>){
    delete<Data.Word.Id> {word ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val wordResponse = controller.get().deleteWord(principal.user, word.id)
        val response = generateHttpResponse(wordResponse)
        call.respond(response.code, wordResponse)
    }
}
private fun Route.wordOperations(controller: Lazy<DataController>){
    postWord(controller)
    getWordById(controller)
    updateWordById(controller)
    deleteWordById(controller)
}

private fun Route.postSentence(controller: Lazy<DataController>){
    post<Data.Sentence.Create>{
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val sentence = runCatching { call.receive<SentenceRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val sentenceResponse = controller.get().addSentence(principal.user, sentence)
        val response = generateHttpResponse(sentenceResponse)
        call.respond(response.code, sentenceResponse)
    }
}
private fun Route.getSentenceById(controller: Lazy<DataController>){
    get<Data.Sentence.Id> {sentence->
        // Gets all the progress information for each sentence the user is learning
        val sentenceResponse = controller.get().getSentence(sentence.id)
        val response = generateHttpResponse(sentenceResponse)
        call.respond(response.code, sentenceResponse)
    }
}
private fun Route.updateSentenceById(controller: Lazy<DataController>){
    put<Data.Sentence.Id>{sentence ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val sentenceRequest  = runCatching { call.receive<SentenceRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val sentenceResponse = controller.get().updateSentence(principal.user, sentence.id, sentenceRequest)
        val response = generateHttpResponse(sentenceResponse)
        call.respond(response.code, sentenceResponse)
    }
}
private fun Route.deleteSentenceById(controller: Lazy<DataController>){
    delete<Data.Sentence.Id> {sentence ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val sentenceResponse = controller.get().deleteSentence(principal.user, sentence.id)
        val response = generateHttpResponse(sentenceResponse)
        call.respond(response.code, sentenceResponse)
    }
}
private fun Route.sentenceOperations(controller: Lazy<DataController>){
    postSentence(controller)
    getSentenceById(controller)
    updateSentenceById(controller)
    deleteSentenceById(controller)
}

private fun Route.postFlag(controller: Lazy<DataController>){
    post<Data.Flag.Create>{
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val flag = runCatching { call.receive<FlagRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_DATA)
        }
        val flagResponse = controller.get().addFlag(principal.user, flag)
        val response = generateHttpResponse(flagResponse)
        call.respond(response.code, flagResponse)
    }
}
private fun Route.getAllFlags(controller: Lazy<DataController>){
    get <Data.Flag>{
        val flagResponse = controller.get().getAllFlags()
        val response = generateHttpResponse(flagResponse)
        call.respond(response.code, flagResponse)
    }
}
private fun Route.getFlagById(controller: Lazy<DataController>){
    get<Data.Flag.Id> {flag->
        val flagResponse = controller.get().getFlag(flag.id)
        val response = generateHttpResponse(flagResponse)
        call.respond(response.code, flagResponse)
    }
}
private fun Route.flagOperations(controller: Lazy<DataController>){
    postFlag(controller)
    getFlagById(controller)
    getAllFlags(controller)
}
