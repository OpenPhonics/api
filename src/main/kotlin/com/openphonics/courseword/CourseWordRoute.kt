package com.openphonics.courseword

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

@Resource(Routes.COURSE_WORDS)
class CourseWord(){
    @Resource(Routes.ID)
    class Id(val parent: CourseWord = CourseWord(), val id: Int)
    @Resource(Routes.COURSE)
    class Course(val parent: CourseWord = CourseWord()){
        @Resource(Routes.ID)
        class Id(val parent: Course = Course(), val id: Int)
    }
}

@KtorExperimentalAPI
fun Route.CourseWordAPI(courseWordController: Lazy<CourseWordController> = controllers.courseWordController()) {
    put(courseWordController)
    get(courseWordController)
    delete(courseWordController)
    post(courseWordController)
    all(courseWordController)
}
private fun Route.put(controller: Lazy<CourseWordController>) {
    put<CourseWord> {
        val request = runCatching { call.receive<CourseWordCreate>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_LANGUAGE_DETAILS)
        }
        val response = controller.get().create(request)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.all(controller: Lazy<CourseWordController>) {
    get<CourseWord.Course.Id> { param ->
        val response = controller.get().all(param.id)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.get(controller: Lazy<CourseWordController>) {
    get<CourseWord.Id> { param ->
        val response = controller.get().get(param.id)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.delete(controller: Lazy<CourseWordController>) {
    delete<CourseWord.Id> { param ->
        val response = controller.get().delete(param.id)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.post(controller: Lazy<CourseWordController>) {
    post<CourseWord.Id> { param ->
        val request = runCatching { call.receive<CourseWordUpdate>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_LANGUAGE_DETAILS)
        }
        val response = controller.get().update(param.id, request)
        call.respond(generateHttpCode(response), response)
    }
}