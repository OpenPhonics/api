package com.openphonics.tests

import com.openphonics.ApplicationTest
import com.openphonics.ApplicationTest.Companion.extractResponse
import com.openphonics.Testing.INVALID_BLANK_NAME
import com.openphonics.Testing.INVALID_LONG_NAME
import com.openphonics.Testing.INVALID_NO_SPACE_NAME
import com.openphonics.Testing.INVALID_NUMERIC_NAME
import com.openphonics.Testing.INVALID_SHORT_NAME
import com.openphonics.Testing.VALID_NAME
import com.openphonics.model.request.AdminSignUpRequest
import com.openphonics.model.request.LoginRequest
import com.openphonics.model.response.AuthResponse
import com.openphonics.route.auth.Routing
import com.openphonics.tests.Auth.invalidRegisterAdminBlankName
import com.openphonics.tests.Auth.invalidRegisterAdminInvalidClassCode
import com.openphonics.tests.Auth.invalidRegisterAdminLongName
import com.openphonics.tests.Auth.invalidRegisterAdminNoSpaceName
import com.openphonics.tests.Auth.invalidRegisterAdminNumericName
import com.openphonics.tests.Auth.invalidRegisterAdminShortName
import com.openphonics.tests.Auth.invalidRegisterAdminWrongClassCode
import com.openphonics.tests.Auth.login
import com.openphonics.tests.Auth.registerAdmin
import com.openphonics.tests.Auth.testLoginAdmin
import com.openphonics.tests.Auth.testRegisterAdmin
import com.openphonics.tests.Auth.validLoginAdmin
import com.openphonics.tests.Auth.validRegisterAdmin
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

object Auth {
    const val REGISTER_URL = "${Routing.AUTH}/${Routing.REGISTER}/${Routing.ADMIN}"
    const val LOGIN_URL = "${Routing.AUTH}/${Routing.LOGIN}"
    val validRegisterAdmin = AdminSignUpRequest(
        VALID_NAME,
        ClassroomTest.VALID_ADMIN_CLASS_CODE
    )

    val invalidRegisterAdminBlankName = AdminSignUpRequest(
        INVALID_BLANK_NAME,
        ClassroomTest.VALID_ADMIN_CLASS_CODE
    )

    val invalidRegisterAdminShortName = AdminSignUpRequest(
        INVALID_SHORT_NAME,
        ClassroomTest.VALID_ADMIN_CLASS_CODE
    )

    val invalidRegisterAdminLongName = AdminSignUpRequest(
        INVALID_LONG_NAME,
        ClassroomTest.VALID_ADMIN_CLASS_CODE
    )

    val invalidRegisterAdminNumericName = AdminSignUpRequest(
        INVALID_NUMERIC_NAME,
        ClassroomTest.VALID_ADMIN_CLASS_CODE
    )

    val invalidRegisterAdminNoSpaceName = AdminSignUpRequest(
        INVALID_NO_SPACE_NAME,
        ClassroomTest.VALID_ADMIN_CLASS_CODE
    )
    val invalidRegisterAdminWrongClassCode = AdminSignUpRequest(
        VALID_NAME,
        ClassroomTest.VALID_CLASS_CODE
    )

    val invalidRegisterAdminInvalidClassCode = AdminSignUpRequest(
        VALID_NAME,
        ClassroomTest.BLANK_CLASS_CODE
    )

    val validLoginAdmin = LoginRequest(
        VALID_NAME,
        ClassroomTest.VALID_ADMIN_CLASS_CODE
    )
    val registerAdmin: suspend (AdminSignUpRequest?, HttpClient) -> HttpResponse = { data, client ->
        client.post(REGISTER_URL) {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }
    val login: suspend (LoginRequest?, HttpClient) -> HttpResponse = { data, client ->
        client.post(LOGIN_URL) {
            contentType(ContentType.Application.Json)
            setBody(data)
        }
    }
    val testRegisterAdmin: suspend (AdminSignUpRequest?, HttpStatusCode, HttpClient) -> Unit = {request, code, client ->
        val response = registerAdmin(request, client)
        assertEquals(code, response.status)
    }
    val testLoginAdmin: suspend (AdminSignUpRequest?, LoginRequest?, HttpStatusCode, HttpClient) -> String = {admin, login, code, client ->
        registerAdmin(admin, client)
        val token = login(login, client)
        assertEquals(code, token.status)
        val response = token.body<AuthResponse>()
        assertNotEquals(null, response.token)
        response.token!!
    }
}
class AuthTest {
    @Test
    fun testRegisterAdminWithValidCredentials() = ApplicationTest.test() { client ->
        testRegisterAdmin(validRegisterAdmin, HttpStatusCode.OK, client)
    }
    @Test
    fun testRegisterAdminWithInvalidBlankName() = ApplicationTest.test() { client ->
        testRegisterAdmin(invalidRegisterAdminBlankName, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testRegisterAdminWithInvalidShortName() = ApplicationTest.test() { client ->
        testRegisterAdmin(invalidRegisterAdminShortName, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testRegisterAdminWithInvalidLongName() = ApplicationTest.test() { client ->
        testRegisterAdmin(invalidRegisterAdminLongName, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testRegisterAdminWithInvalidNumericName() = ApplicationTest.test() { client ->
        testRegisterAdmin(invalidRegisterAdminNumericName, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testRegisterAdminWithInvalidNameTaken() = ApplicationTest.test() { client ->
        registerAdmin(validRegisterAdmin, client)
        testRegisterAdmin(validRegisterAdmin, HttpStatusCode.BadRequest, client)
    }

    @Test
    fun testRegisterAdminWithInvalidNoSpaceName() = ApplicationTest.test() { client ->
        testRegisterAdmin(invalidRegisterAdminNoSpaceName, HttpStatusCode.BadRequest, client)
    }

    @Test
    fun testRegisterAdminWithInvalidNonAdminClassCode() = ApplicationTest.test() { client ->
        testRegisterAdmin(invalidRegisterAdminWrongClassCode, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testRegisterAdminWithInvalidBlankClassCode() = ApplicationTest.test() { client ->
        testRegisterAdmin(invalidRegisterAdminInvalidClassCode, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testRegisterAdminWithMissingCredentials() = ApplicationTest.test() { client ->
        testRegisterAdmin(null, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testLoginAdmin() = ApplicationTest.test() { client ->
        testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
    }
    @Test
    fun testLoginAdminUserDoesNotExist() = ApplicationTest.test() { client ->
        testLoginAdmin(null, validLoginAdmin, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testLoginAdminMissingCredentials() = ApplicationTest.test() { client ->
        testLoginAdmin(validRegisterAdmin, null, HttpStatusCode.BadRequest, client)
    }
}