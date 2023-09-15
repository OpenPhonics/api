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

package com.openphonics.controller

import com.openphonics.auth.Encryptor
import com.openphonics.auth.JWTController
import com.openphonics.data.dao.DataDao
import com.openphonics.data.dao.UserDao
import com.openphonics.data.entity.data.EntityLanguage
import com.openphonics.data.model.user.User
import com.openphonics.di.module.AdminKey
import com.openphonics.exception.BadRequestException
import com.openphonics.exception.NotFoundException
import com.openphonics.exception.UnauthorizedActivityException
import com.openphonics.model.request.*
import com.openphonics.model.response.AuthResponse
import com.openphonics.model.response.Classroom
import com.openphonics.model.response.ClassroomResponse
import com.openphonics.model.response.StrIdResponse
import com.openphonics.utils.containsOnlyLetters
import com.openphonics.utils.isAlphaNumeric
import com.openphonics.utils.isFullName
import io.ktor.util.*
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Controller for authentication i.e. User's management
 */
@KtorExperimentalAPI
@Singleton
class AuthController @Inject constructor(
    @AdminKey private val adminKey: String,
    private val userDao: UserDao,
    private val dataDao: DataDao,
    private val jwt: JWTController,
    private val encryptor: Encryptor,
) {
    fun registerUser(user: UserSignUpRequest): AuthResponse {
        val name = user.name
        val classCode = user.classCode
        val native = user.native
        val language = user.language
        return try {
            val encryptedCode = encryptor.encrypt(classCode)
            validateCredentialsOrThrowException(name, classCode)
            validateNativeOrThrowException(native)
            validateLanguageExists(language)
            if (!userDao.isNameAvailable(name, encryptor.encrypt(classCode))) {
                throw BadRequestException("name is not available")
            }
            if (!userDao.doesClassCodeExists(encryptedCode) || encryptedCode == adminKey) {
                throw BadRequestException("Invalid class code")
            }
            val userResponse = userDao.addUser(name, encryptedCode, native, language)
            AuthResponse.success(jwt.sign(userResponse.id), encryptedCode)
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            AuthResponse.failed(nfe.message)
        }
    }
    fun registerAdmin(admin: AdminSignUpRequest): AuthResponse {
        return try {
            val name = admin.name
            val classCode = admin.classCode
            val native = AdminSignUpRequest.ADMIN_NATIVE
            val encryptedCode = encryptor.encrypt(classCode)
            validateCredentialsOrThrowException(name, classCode)
            validateNativeOrThrowException(native)
            if (!userDao.isNameAvailable(name, encryptor.encrypt(classCode))) {
                throw BadRequestException("name is not available")
            }
            if (encryptedCode != adminKey) {
                throw BadRequestException("Invalid class code")
            } else if (!userDao.doesClassCodeExists(encryptedCode)){
                userDao.addClass(encryptedCode, "Admins")
            }
            val user = userDao.addAdmin(name, encryptedCode, native)
            AuthResponse.success(jwt.sign(user.id), encryptedCode)
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            AuthResponse.failed(nfe.message)
        }
    }
    fun deleteUserFromAdmin(admin: User, userId: String): StrIdResponse {
        return try {
            if (!admin.isAdmin)
                throw UnauthorizedActivityException("Must be admin to delete other users")
            if (!userDao.exists(UUID.fromString(userId))){
                throw NotFoundException("User does not exist")
            }
            if (userDao.deleteByID(userId)) {
                StrIdResponse.success(userId)
            } else {
                StrIdResponse.failed("Error Occurred")
            }
        } catch (nfe: NotFoundException) {
            StrIdResponse.failed(nfe.message)
        }
    }
    fun delete(user: User): StrIdResponse {
        return try {
            if (!userDao.exists(UUID.fromString(user.id))){
                throw NotFoundException("User does not exist")
            }
            if (userDao.deleteByID(user.id)) {
                StrIdResponse.success(user.id)
            } else {
                StrIdResponse.failed("Error Occurred")
            }
        } catch (nfe: NotFoundException) {
            StrIdResponse.failed(nfe.message)
        }
    }
    fun login(login: LoginRequest): AuthResponse {
        val name = login.name
        val classCode = login.classCode
        return try {
            validateCredentialsOrThrowException(name, classCode)
            val user = userDao.findByNameAndClassCode(name, encryptor.encrypt(classCode))
                ?: throw UnauthorizedActivityException("Invalid credentials")
            AuthResponse.success(jwt.sign(user.id), "Login successful")
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            AuthResponse.unauthorized(uae.message)
        }
    }

    fun getClassroom(user: User, classCode: String): ClassroomResponse {
        return try {
            val encryptedCode = encryptor.encrypt(classCode)
            if (!userDao.doesClassCodeExists(encryptedCode)) {
                throw BadRequestException("Class code doesn't exist")
            }
            val classroom = userDao.getClass(encryptedCode)
            ClassroomResponse.success(Classroom.create(classCode, classroom))
        } catch (bre: BadRequestException) {
            ClassroomResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            ClassroomResponse.unauthorized(uae.message)
        }
    }
    fun addClassroom(user: User, classroom: ClassroomRequest): StrIdResponse {
        val className = classroom.className
        val classCode = classroom.classCode
        return try {
            val encryptedCode = encryptor.encrypt(classCode)
            validateClass(classCode, className)
            if (!user.isAdmin){
                throw UnauthorizedActivityException("User must be admin to add classroom")
            }
            if (userDao.doesClassCodeExists(encryptedCode)) {
                throw BadRequestException("Class code has already been used")
            }
            userDao.addClass(encryptedCode, className)
            StrIdResponse.success(classCode)
        } catch (bre: BadRequestException) {
            StrIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            StrIdResponse.unauthorized(uae.message)
        }
    }
    fun updateClassroom(user: User, classCode: String, classroom: UpdateClassroomRequest): StrIdResponse {
        return try {
            val className = classroom.className
            val encryptedCode = encryptor.encrypt(classCode)
            validateClass(classCode, className)
            if (!user.isAdmin) {
                throw UnauthorizedActivityException("User must be admin to update classroom")
            }
            if (!userDao.doesClassCodeExists(encryptedCode)) {
                throw BadRequestException("Class code doesn't exist")
            }
            userDao.updateClass(encryptedCode, className)
            StrIdResponse.success(classCode)
            } catch (bre: BadRequestException) {
                StrIdResponse.failed(bre.message)
            } catch (uae: UnauthorizedActivityException) {
                StrIdResponse.unauthorized(uae.message)
            }
        }
    fun deleteClassroom(user: User, classCode: String): StrIdResponse {
        return try {
            val encryptedCode = encryptor.encrypt(classCode)
            if (!user.isAdmin)
                throw UnauthorizedActivityException("Must be admin to delete classroom")
            if (!userDao.doesClassCodeExists(encryptedCode)){
                throw NotFoundException("Classroom does not exist")
            }
            if (userDao.deleteClass(encryptedCode)) {
                StrIdResponse.success(classCode)
            } else {
                StrIdResponse.failed("Error Occurred")
            }
        } catch (nfe: NotFoundException) {
            StrIdResponse.failed(nfe.message)
        }
    }
    private fun validateLanguageExists(language: Int){
        if (!dataDao.exists(language, EntityLanguage)) {
            throw NotFoundException("Data not exist with ID '$language'")
        }
    }
    private fun validateClass(classCode: String, className: String){
        val message = when {
            classCode.length !=6 -> "Class code length must be 6 characters"
            !classCode.isAlphaNumeric() -> "Class code must be alpha numeric"
            className.length > 30 -> "Class name must be less then 30 characters long"
            !className.containsOnlyLetters() -> "Class name cannot contain numbers or special characters"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateNativeOrThrowException(native: String){
        val message = when {
            native.length != 2 -> "native must be 2 characters"
            !native.containsOnlyLetters() -> "native cannot contain numbers or special characters"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateCredentialsOrThrowException(name: String, classCode: String) {
        val message = when {
            (name.isBlank() or classCode.isBlank()) -> "name or classCode should not be blank"
            (name.length !in (4..30)) -> "name should be of min 4 and max 30 character in length"
            classCode.length != 6 -> "Class code should be 6 digits long"
            (!name.isFullName()) -> "No special characters allowed in name"
            else -> return
        }
        throw BadRequestException(message)
    }
}