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
import com.openphonics.model.response.AuthResponse
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
    fun registerUser(name: String, classCode: String, native: String, language: Int): AuthResponse {
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
            val user = userDao.addUser(name, encryptedCode, native, language)
            AuthResponse.success(jwt.sign(user.id), encryptedCode)
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            AuthResponse.failed(nfe.message)
        }
    }
    fun registerAdmin(name: String, classCode: String, native: String): AuthResponse {
        return try {
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
    fun login(name: String, classCode: String): AuthResponse {
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
    fun addClass(user: User, classCode: String, className: String): StrIdResponse {
        return try {
            val encryptedCode = encryptor.encrypt(classCode)
            validateClass(classCode, className)
            if (!user.isAdmin){
                throw UnauthorizedActivityException("User must be admin to add classroom")
            }
            if (userDao.doesClassCodeExists(encryptedCode)) {
                throw BadRequestException("Class code has already been used")
            }
            val responseId = userDao.addClass(encryptedCode, className)
            StrIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            StrIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            StrIdResponse.unauthorized(uae.message)
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