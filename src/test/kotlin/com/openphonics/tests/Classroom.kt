package com.openphonics.tests

import com.openphonics.ApplicationTest
import com.openphonics.ApplicationTest.Companion.extractResponse
import com.openphonics.model.request.AdminSignUpRequest
import com.openphonics.model.request.ClassroomRequest
import com.openphonics.model.response.StrIdResponse
import com.openphonics.route.auth.Routing
import com.openphonics.tests.Auth.login
import com.openphonics.tests.Auth.testLoginAdmin
import com.openphonics.tests.Auth.validLoginAdmin
import com.openphonics.tests.Auth.validRegisterAdmin
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ClassroomTest {
    companion object {
        const val VALID_CLASS_CODE = "eg3a1r"
        const val VALID_ADMIN_CLASS_CODE = "e5fe1k"
        const val BLANK_CLASS_CODE = ""

        const val VALID_CLASS_NAME = "Bharathi Primary"
        const val INVALID_LONG_CLASS_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        const val INVALID_NUMERIC_CLASS_NAME = "A4"

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
        val testCreateClass: suspend (String?, ClassroomRequest?, HttpStatusCode, HttpClient) -> String = {tok, request, code, client ->
            val token = tok?: testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
            val classResponse = createClass(token, request, client)
            val response = extractResponse<StrIdResponse>(classResponse)
            assertEquals(code, classResponse.status)
            assertNotEquals(null, response.id)
            response.id!!
        }
    }
    @Test
    fun testCreateClassWithValidCredentials() = ApplicationTest.test() { client ->
        testCreateClass(null, validClassroom, HttpStatusCode.OK, client)

    }
    @Test
    fun testCreateClassWithInvalidBlankClassCode() = ApplicationTest.test() { client ->
        testCreateClass(null, invalidClassroomBlankClassCode, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testCreateClassWithInvalidLongClassName() = ApplicationTest.test() { client ->
        testCreateClass(null, invalidClassroomLongName, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testCreateClassWithInvalidNumericClassName() = ApplicationTest.test() { client ->
        testCreateClass(null, invalidClassroomNumericName, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testCreateClassWithMissingCredentials() = ApplicationTest.test() { client ->
        testCreateClass(null, null, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testCreateClassAlreadyExists() = ApplicationTest.test() { client ->
        val token = testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
        testCreateClass(token, validClassroom, HttpStatusCode.OK, client)
        testCreateClass(token, validClassroom, HttpStatusCode.OK, client)
    }
    @Test
    fun testCreateClassWithInvalidPower() = ApplicationTest.test() { client ->
//        AdminTest.registerAdmin(AdminTest.validRegisterAdmin, client)
//        val token = AdminTest.loginAdmin(AdminTest.validLoginAdmin, client)
//        val response = createClass(token.body(), null, client)
//        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}