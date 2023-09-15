package com.openphonics.model.response

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


import com.openphonics.data.entity.user.EntityClassroom
import com.openphonics.data.entity.user.EntityUser
import kotlinx.serialization.Serializable

/**
 * Response model used in Authentication API. For e.g. Login/Register.
 */
@Serializable
data class AuthResponse(
    override val status: State,
    override val message: String,
    val token: String? = null
) : Response {

    companion object {

        fun failed(message: String) = AuthResponse(
            State.FAILED,
            message
        )

        fun unauthorized(message: String) = AuthResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(token: String, message: String) = AuthResponse(
            State.SUCCESS,
            message,
            token
        )
    }
}
@Serializable
data class Student(
    val name: String,
    val classCode: String,
    val native: String,
    val progress: List<LanguageProgress>,
){
    companion object {
        fun create(student: com.openphonics.data.model.user.Student) = Student(
            student.name,
            student.classCode,
            student.native,
            student.progress.map {
                LanguageProgress.create(it)
            }
        )
    }
}
@Serializable
data class Classroom(
    val className: String,
    val classCode: String,
    val students: List<Student>,

) {
    companion object {
        fun create(classCode: String, classroom: com.openphonics.data.model.user.Classroom) = Classroom(
            classroom.className,
            classCode,
            classroom.students.map {
                Student.create(it)
            },
        )
    }
}
@Serializable
data class ClassroomResponse(
    override val status: State,
    override val message: String,
    val classroom: List<Classroom> = emptyList()
    )  : Response {
    companion object {
        fun unauthorized(message: String) = ClassroomResponse(
            State.UNAUTHORIZED,
            message
        )

        fun success(classroom: List<Classroom>) = ClassroomResponse(
            State.SUCCESS,
            "Task successful",
            classroom
        )
        fun success(classroom: Classroom) = ClassroomResponse(
            State.SUCCESS,
            "Task successful",
            listOf(classroom)
        )

        fun failed(message: String) = ClassroomResponse(
            State.FAILED,
            message
        )

        fun notFound(message: String) = ClassroomResponse(
            State.NOT_FOUND,
            message
        )
    }
}