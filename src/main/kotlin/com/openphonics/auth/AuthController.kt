package com.openphonics.auth

import com.openphonics.common.core.DataResponse
import com.openphonics.common.exception.*
import com.openphonics.common.utils.ext.isAlphaNumeric
import io.ktor.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Controller for authentication i.e. User's management
 */
@KtorExperimentalAPI
@Singleton
class AuthController @Inject constructor(
    private val userDao: UserDao,
    private val jwt: JWTController,
    private val encryptor: Encryptor
) {

    fun register(username: String): DataResponse<String> {
        return try {
            validateCredentialsOrThrowException(username)

            if (!userDao.isUsernameAvailable(username)) {
                throw BadRequestException("Username is not available")
            }

            val user = userDao.addUser(username)
            DataResponse.success(jwt.sign(user.id))
        } catch (bre: BadRequestException) {
            DataResponse.failed(bre.message)
        }
    }
    fun authenticate(user: User?): DataResponse<String> {
        return try {
            DataResponse.success(jwt.sign(user!!.id))
        } catch (uae: UnauthorizedActivityException) {
            DataResponse.unauthorized(uae.message)
        }
    }
    fun login(username: String): DataResponse<String> {
        return try {
            validateCredentialsOrThrowException(username)

            val user = userDao.findByUsername(username)
                ?: throw UnauthorizedActivityException("Invalid credentials")

            DataResponse.success(jwt.sign(user.id))
        } catch (bre: BadRequestException) {
            DataResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            DataResponse.unauthorized(uae.message)
        }
    }

    private fun validateCredentialsOrThrowException(username: String) {
        val message = when {
            (username.isBlank()) -> "Username should not be blank"
            (username.length !in (4..30)) -> "Username should be of min 4 and max 30 character in length"
            (!username.isAlphaNumeric()) -> "No special characters allowed in username"
            else -> return
        }

        throw BadRequestException(message)
    }
}