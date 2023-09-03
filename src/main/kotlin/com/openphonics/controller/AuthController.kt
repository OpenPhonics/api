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
import com.openphonics.exception.BadRequestException
import com.openphonics.exception.NotFoundException
import com.openphonics.exception.UnauthorizedActivityException
import com.openphonics.model.response.AuthResponse
import com.openphonics.model.response.IntIdResponse
import com.openphonics.model.response.StrIdResponse
import com.openphonics.utils.isAlphaNumeric
import com.openphonics.utils.isFullName
import com.openphonics.data.dao.DataDao
import com.openphonics.data.dao.UserDao
import com.openphonics.data.entity.data.EntityLanguage
import io.ktor.util.KtorExperimentalAPI
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Controller for authentication i.e. User's management
 */
@KtorExperimentalAPI
@Singleton
class AuthController @Inject constructor(
    private val userDao: UserDao,
    private val dataDao: DataDao,
    private val jwt: JWTController,
    private val encryptor: Encryptor
) {

    fun register(name: String, classCode: String, native: String, isAdmin: Boolean, language: Int): AuthResponse {
        return try {
            val encryptedCode = encryptor.encrypt(classCode)
            validateCredentialsOrThrowException(name, classCode)
//            validateLanguageExists(language)
            if (!userDao.isNameAvailable(name, encryptor.encrypt(classCode))) {
                throw BadRequestException("name is not available")
            }
            if (!userDao.doesClassCodeExists(encryptedCode)) {
                throw BadRequestException("Invalid class code")
            }
//            val langExists = !dataDao.exists(language, EntityLanguage)
//            if (langExists && isAdmin){
            val user = userDao.addUserWithoutLanguage(name, encryptedCode, native, isAdmin)
            AuthResponse.success(jwt.sign(user.id), "New user created")
//            } else if (langExists){
//                throw BadRequestException("Language does not exist")
//            } else {
//                val user = userDao.addUser(name, encryptedCode, native, isAdmin, language)
//                AuthResponse.success(jwt.sign(user.id), "New user created")
//            }
//            throw BadRequestException("Language does not exist")
        } catch (bre: BadRequestException) {
            AuthResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            AuthResponse.failed(nfe.message)
        }
    }
    private fun validateLanguageExists(language: Int){
        if (!dataDao.exists(language, String)) {
            throw NotFoundException("Data not exist with ID '$language'")
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

    fun addClass(classCode: String, className: String): StrIdResponse {
        return try {
//            validateClass(classCode, className)
            val responseId = userDao.addClass(encryptor.encrypt(classCode), className)
            StrIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            StrIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            StrIdResponse.unauthorized(uae.message)
        }
    }
    private fun validateClass(classCode: String, className: String){
        val message = when {
            classCode.length !=6 -> "Class code length must be 6 characters"
            classCode.isAlphaNumeric() -> "Class code must be alpha numeric"
            className.length > 30 -> "Class name must be less then 30 characters long"
            else -> return
        }

        throw BadRequestException(message)
    }

    private fun validateCredentialsOrThrowException(name: String, classCode: String) {
        val message = when {
            (name.isBlank() or classCode.isBlank()) -> "name or classCode should not be blank"
            (name.length !in (4..30)) -> "name should be of min 4 and max 30 character in length"
            (classCode.length !in (6..50)) -> "Class code should be 6 digits long"
            (!name.isFullName()) -> "No special characters allowed in name"
            else -> return
        }
        throw BadRequestException(message)
    }
}