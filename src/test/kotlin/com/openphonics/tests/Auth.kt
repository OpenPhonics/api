package com.openphonics.tests

import com.openphonics.ApplicationTest
import com.openphonics.ApplicationTest.Companion.test
import com.openphonics.ApplicationTest.Companion.extractResponse
import com.openphonics.Testing.BLANK_CLASS_CODE
import com.openphonics.Testing.INVALID_BLANK_NAME
import com.openphonics.Testing.INVALID_LONG_CLASS_NAME
import com.openphonics.Testing.INVALID_LONG_NAME
import com.openphonics.Testing.INVALID_NO_SPACE_NAME
import com.openphonics.Testing.INVALID_NUMERIC_CLASS_NAME
import com.openphonics.Testing.INVALID_NUMERIC_NAME
import com.openphonics.Testing.INVALID_SHORT_NAME
import com.openphonics.Testing.VALID_ADMIN_CLASS_CODE
import com.openphonics.Testing.VALID_CLASS_CODE
import com.openphonics.Testing.VALID_CLASS_NAME
import com.openphonics.Testing.VALID_NAME
import com.openphonics.model.request.AdminSignUpRequest
import com.openphonics.model.request.ClassroomRequest
import com.openphonics.model.request.LoginRequest
import com.openphonics.model.response.AuthResponse
import com.openphonics.model.response.StrIdResponse
import com.openphonics.route.auth.Routing
import com.openphonics.tests.Auth.invalidRegisterAdminBlankName
import com.openphonics.tests.Auth.invalidRegisterAdminInvalidClassCode
import com.openphonics.tests.Auth.invalidRegisterAdminLongName
import com.openphonics.tests.Auth.invalidRegisterAdminNoSpaceName
import com.openphonics.tests.Auth.invalidRegisterAdminNumericName
import com.openphonics.tests.Auth.invalidRegisterAdminShortName
import com.openphonics.tests.Auth.invalidRegisterAdminWrongClassCode
import com.openphonics.tests.Auth.registerAdmin
import com.openphonics.tests.Auth.testLoginAdmin
import com.openphonics.tests.Auth.testRegisterAdmin
import com.openphonics.tests.Auth.validLoginAdmin
import com.openphonics.tests.Auth.validRegisterAdmin
import com.openphonics.tests.Classroom.invalidClassroomBlankClassCode
import com.openphonics.tests.Classroom.invalidClassroomLongName
import com.openphonics.tests.Classroom.invalidClassroomNumericName
import com.openphonics.tests.Classroom.testCreateClass
import com.openphonics.tests.Classroom.validClassroom
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import kotlin.test.Test
import kotlin.test.assertEquals

object Classroom {
    const val CREATE_CLASS_URL = "${Routing.AUTH}/${Routing.CLASS}"
    val validClassroom = ClassroomRequest(
        VALID_CLASS_NAME,
        VALID_CLASS_CODE
    )
    val invalidClassroomBlankClassCode = ClassroomRequest(
        VALID_CLASS_NAME,
        BLANK_CLASS_CODE
    )
    val invalidClassroomLongName = ClassroomRequest(
        INVALID_LONG_CLASS_NAME,
        VALID_CLASS_CODE
    )
    val invalidClassroomNumericName = ClassroomRequest(
        INVALID_NUMERIC_CLASS_NAME,
        VALID_CLASS_CODE
    )

    val createClass: suspend (String, ClassroomRequest?, HttpClient) -> HttpResponse = { token, data, client ->
        client.post(CREATE_CLASS_URL) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val testCreateClass: suspend (String?, ClassroomRequest?, HttpStatusCode, HttpClient) -> String = { tok, request, code, client ->
        val token = tok?: testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
        val classResponse = createClass(token, request, client)
        val response = extractResponse<StrIdResponse>(classResponse)
        assertEquals(code, classResponse.status, classResponse.body())
//        assertNotNull(response.id)
        response.id?: ""
    }
}
object Auth {
    const val REGISTER_URL = "${Routing.AUTH}/${Routing.REGISTER}/${Routing.ADMIN}"
    const val LOGIN_URL = "${Routing.AUTH}/${Routing.LOGIN}"
    val validRegisterAdmin = AdminSignUpRequest(
        VALID_NAME,
        VALID_ADMIN_CLASS_CODE
    )

    val invalidRegisterAdminBlankName = AdminSignUpRequest(
        INVALID_BLANK_NAME,
        VALID_ADMIN_CLASS_CODE
    )

    val invalidRegisterAdminShortName = AdminSignUpRequest(
        INVALID_SHORT_NAME,
        VALID_ADMIN_CLASS_CODE
    )

    val invalidRegisterAdminLongName = AdminSignUpRequest(
        INVALID_LONG_NAME,
        VALID_ADMIN_CLASS_CODE
    )

    val invalidRegisterAdminNumericName = AdminSignUpRequest(
        INVALID_NUMERIC_NAME,
        VALID_ADMIN_CLASS_CODE
    )

    val invalidRegisterAdminNoSpaceName = AdminSignUpRequest(
        INVALID_NO_SPACE_NAME,
        VALID_ADMIN_CLASS_CODE
    )
    val invalidRegisterAdminWrongClassCode = AdminSignUpRequest(
        VALID_NAME,
        VALID_CLASS_CODE
    )

    val invalidRegisterAdminInvalidClassCode = AdminSignUpRequest(
        VALID_NAME,
        BLANK_CLASS_CODE
    )

    val validLoginAdmin = LoginRequest(
        VALID_NAME,
        VALID_ADMIN_CLASS_CODE
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
        assertEquals(code, response.status, response.body())
    }
    val testLoginAdmin: suspend (AdminSignUpRequest?, LoginRequest?, HttpStatusCode, HttpClient) -> String = {admin, login, code, client ->
        registerAdmin(admin, client)
        val token = login(login, client)
        assertEquals(code, token.status)
        val response = token.body<AuthResponse>()
//        assertNotEquals(null, response.token)
        response.token ?: ""
    }
}
//class AuthTest {
//    @Test
//    fun testCreateClassWithValidCredentials() = test() { client ->
//        testCreateClass(null, validClassroom, HttpStatusCode.OK, client)
//
//    }
//    @Test
//    fun testCreateClassWithInvalidBlankClassCode() = test() { client ->
//        testCreateClass(null, invalidClassroomBlankClassCode, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateClassWithInvalidLongClassName() = test() { client ->
//        testCreateClass(null, invalidClassroomLongName, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateClassWithInvalidNumericClassName() = test() { client ->
//        testCreateClass(null, invalidClassroomNumericName, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateClassWithMissingCredentials() = test() { client ->
//        testCreateClass(null, null, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateClassAlreadyExists() = test() { client ->
//        val token = testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
//        testCreateClass(token, validClassroom, HttpStatusCode.OK, client)
//        testCreateClass(token, validClassroom, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateClassWithInvalidPower() = test() { client ->
////        AdminTest.registerAdmin(AdminTest.validRegisterAdmin, client)
////        val token = AdminTest.loginAdmin(AdminTest.validLoginAdmin, client)
////        val response = createClass(token.body(), null, client)
////        assertEquals(HttpStatusCode.BadRequest, response.status)
//    }
//    @Test
//    fun testRegisterAdminWithValidCredentials() = test() { client ->
//        testRegisterAdmin(validRegisterAdmin, HttpStatusCode.OK, client)
//    }
//    @Test
//    fun testRegisterAdminWithInvalidBlankName() = test() { client ->
//        testRegisterAdmin(invalidRegisterAdminBlankName, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testRegisterAdminWithInvalidShortName() = test() { client ->
//        testRegisterAdmin(invalidRegisterAdminShortName, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testRegisterAdminWithInvalidLongName() = test() { client ->
//        testRegisterAdmin(invalidRegisterAdminLongName, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testRegisterAdminWithInvalidNumericName() = test() { client ->
//        testRegisterAdmin(invalidRegisterAdminNumericName, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testRegisterAdminWithInvalidNameTaken() = test() { client ->
//        registerAdmin(validRegisterAdmin, client)
//        testRegisterAdmin(validRegisterAdmin, HttpStatusCode.BadRequest, client)
//    }
//
//    @Test
//    fun testRegisterAdminWithInvalidNoSpaceName() = test() { client ->
//        testRegisterAdmin(invalidRegisterAdminNoSpaceName, HttpStatusCode.BadRequest, client)
//    }
//
//    @Test
//    fun testRegisterAdminWithInvalidNonAdminClassCode() = test() { client ->
//        testRegisterAdmin(invalidRegisterAdminWrongClassCode, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testRegisterAdminWithInvalidBlankClassCode() = test() { client ->
//        testRegisterAdmin(invalidRegisterAdminInvalidClassCode, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testRegisterAdminWithMissingCredentials() = test() { client ->
//        testRegisterAdmin(null, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testLoginAdmin() = test() { client ->
//        testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
//    }
//    @Test
//    fun testLoginAdminUserDoesNotExist() = test() { client ->
//        testLoginAdmin(null, validLoginAdmin, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testLoginAdminMissingCredentials() = test() { client ->
//        testLoginAdmin(validRegisterAdmin, null, HttpStatusCode.BadRequest, client)
//    }
//}