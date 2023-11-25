package com.openphonics.course

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

@Resource(Routes.COURSES)
class Course(){
    @Resource(Routes.ID)
    class Id(val parent: Course = Course(), val id: Int)
    @Resource(Routes.CODE)
    class Code(val parent: Course = Course()){
        @Resource(Routes.ID)
        class Id(val parent: Code = Code(),  val id: String)
    }
}

@KtorExperimentalAPI
fun Route.CourseAPI(courseController: Lazy<CourseController> = controllers.courseController()) {
    put(courseController)
    get(courseController)
    delete(courseController)
    post(courseController)
    all(courseController)
}
private fun Route.put(controller: Lazy<CourseController>) {
    put<Course> {
        val request = runCatching { call.receive<CourseCreate>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_LANGUAGE_DETAILS)
        }
        val response = controller.get().create(request)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.all(controller: Lazy<CourseController>) {
    get<Course.Code.Id> { param ->
        val response = controller.get().all(param.id)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.get(controller: Lazy<CourseController>) {
    get<Course.Id> { param ->
        val response = controller.get().get(param.id)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.delete(controller: Lazy<CourseController>) {
    delete<Course.Id> { param ->
        val response = controller.get().delete(param.id)
        call.respond(generateHttpCode(response), response)
    }
}
private fun Route.post(controller: Lazy<CourseController>) {
    post<Course.Id> { param ->
        val request = runCatching { call.receive<CourseUpdate>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_LANGUAGE_DETAILS)
        }
        val response = controller.get().update(param.id, request)
        call.respond(generateHttpCode(response), response)
    }
}