/*
 * Copyright 2020 Shreyas Patil
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.openphonics.route.auth

import com.openphonics.auth.principal.UserPrincipal
import com.openphonics.controller.AuthController
import com.openphonics.exception.FailureMessages
import com.openphonics.exception.UnauthorizedActivityException
import com.openphonics.model.request.*
import com.openphonics.model.response.generateHttpResponse
import com.openphonics.plugin.controllers
import dagger.Lazy
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.resources.post
import io.ktor.server.resources.put
import io.ktor.server.resources.get
import io.ktor.server.resources.delete
import io.ktor.server.routing.*
import io.ktor.util.*



@KtorExperimentalAPI
fun Route.AuthApi(authController: Lazy<AuthController> = controllers.authController()) {
    controllers
    login(authController)
    registerUser(authController)
    registerAdmin(authController)
    authenticate {
        getClassroom(authController)
        createClass(authController)
        updateClass(authController)
        deleteClassroom(authController)

        deleteUser(authController)
    }

}

/**
 *@param controller Auth Controller for making changes to auth database
 * creates a classroom using auth/admin/class route
 */
@OptIn(KtorExperimentalAPI::class)
private fun Route.createClass(controller: Lazy<AuthController>){
    post<Auth.Admin.Classroom> {
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val classroomRequest = runCatching { call.receive<ClassroomRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_CREDENTIALS)
        }
        val classroomResponse = controller.get().addClassroom(principal.user, classroomRequest)
        val response = generateHttpResponse(classroomResponse)
        call.respond(response.code, classroomResponse)
    }
}
/**
 *@param controller Auth Controller for making changes to auth database
 * deletes a classroom using auth/admin/class/{classCode} route
 */
@OptIn(KtorExperimentalAPI::class)
private fun Route.deleteClassroom(controller: Lazy<AuthController>){
    delete<Auth.Admin.Classroom.ClassCode> {classCode->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val authResponse = controller.get().deleteClassroom(principal.user, classCode.classCode)
        val response = generateHttpResponse(authResponse)
        call.respond(response.code, authResponse)
    }
}
/**
 *@param controller Auth Controller for making changes to auth database
 * gets a classroom using auth/admin/class route
 */
@OptIn(KtorExperimentalAPI::class)
private fun Route.getClassroom(controller: Lazy<AuthController>){
    get<Auth.Admin.Classroom.ClassCode> {classCode->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val authResponse = controller.get().getClassroom(principal.user, classCode.classCode)
        val response = generateHttpResponse(authResponse)
        call.respond(response.code, authResponse)
    }
}
/**
 *@param controller Auth Controller for making changes to auth database
 * updates a classroom using auth/admin/class/{classCode} route
 */
@OptIn(KtorExperimentalAPI::class)
private fun Route.updateClass(controller: Lazy<AuthController>){
    put<Auth.Admin.Classroom.ClassCode> {classCode ->
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val classroomRequest = runCatching { call.receive<UpdateClassroomRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_CREDENTIALS)
        }
        val classroomResponse = controller.get().updateClassroom(principal.user, classCode.classCode, classroomRequest)
        val response = generateHttpResponse(classroomResponse)
        call.respond(response.code, classroomResponse)
    }
}
/**
 *@param controller Auth Controller for making changes to auth database
 * creates a user using auth/register route
 */
@OptIn(KtorExperimentalAPI::class)
private fun Route.registerUser(controller: Lazy<AuthController>){
    post<Auth.Register> {
        val authRequest = runCatching { call.receive<UserSignUpRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_CREDENTIALS)
        }
        val authResponse = controller.get().registerUser(authRequest)
        val response = generateHttpResponse(authResponse)
        call.respond(response.code, authResponse)
    }
}
/**
 *@param controller Auth Controller for making changes to auth database
 * deletes a user using auth route
 */
@OptIn(KtorExperimentalAPI::class)
private fun Route.deleteUser(controller: Lazy<AuthController>){
    delete<Auth> {
        val principal = call.principal<UserPrincipal>()
            ?: throw UnauthorizedActivityException(FailureMessages.MESSAGE_ACCESS_DENIED)
        val authResponse = controller.get().delete(principal.user)
        val response = generateHttpResponse(authResponse)
        call.respond(response.code, authResponse)
    }
}
/**
 *@param controller Auth Controller for making changes to auth database
 * creates admin user using auth/register/admin route
 */
@OptIn(KtorExperimentalAPI::class)
private fun Route.registerAdmin(controller: Lazy<AuthController>){
    post<Auth.Register.Admin> {
        val authRequest = runCatching { call.receive<AdminSignUpRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_CREDENTIALS)
        }
        val authResponse = controller.get().registerAdmin(authRequest)
        val response = generateHttpResponse(authResponse)
        call.respond(response.code, authResponse)
    }
}
/**
 *@param controller Auth Controller for making changes to auth database
 * login using auth/login route
 */
@OptIn(KtorExperimentalAPI::class)
private fun Route.login(controller: Lazy<AuthController>){
    post<Auth.Login> {
        val authRequest = runCatching { call.receive<LoginRequest>() }.getOrElse {
            throw BadRequestException(FailureMessages.MESSAGE_MISSING_CREDENTIALS)
        }
        val authResponse = controller.get().login(authRequest)
        val response = generateHttpResponse(authResponse)
        call.respond(response.code, authResponse)
    }
}