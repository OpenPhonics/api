package com.openphonics.tests

import com.openphonics.ApplicationTest
import com.openphonics.ApplicationTest.Companion.extractResponse
import com.openphonics.Testing.INVALID_LONG_FLAG
import com.openphonics.Testing.INVALID_LONG_LANGUAGE_ID
import com.openphonics.Testing.INVALID_LONG_LANGUAGE_NAME
import com.openphonics.Testing.INVALID_LONG_NATIVE_ID
import com.openphonics.Testing.INVALID_LONG_TITLE
import com.openphonics.Testing.INVALID_NUMERIC_FLAG
import com.openphonics.Testing.INVALID_NUMERIC_LANGUAGE_ID
import com.openphonics.Testing.INVALID_NUMERIC_LANGUAGE_NAME
import com.openphonics.Testing.INVALID_NUMERIC_NATIVE_ID
import com.openphonics.Testing.INVALID_NUMERIC_TITLE
import com.openphonics.Testing.INVALID_ORDER
import com.openphonics.Testing.VALID_FLAG
import com.openphonics.Testing.VALID_FLAG_IMG
import com.openphonics.Testing.VALID_LANGUAGE_ID
import com.openphonics.Testing.VALID_LANGUAGE_NAME
import com.openphonics.Testing.VALID_NATIVE_ID
import com.openphonics.Testing.VALID_ORDER
import com.openphonics.Testing.VALID_TITLE
import com.openphonics.model.request.DepthRequest
import com.openphonics.model.request.FlagRequest
import com.openphonics.model.request.LanguageRequest
import com.openphonics.model.request.UnitRequest
import com.openphonics.model.response.IntIdResponse
import com.openphonics.model.response.StrIdResponse
import com.openphonics.model.response.UnitResponse
import com.openphonics.route.data.Routing
import com.openphonics.tests.Auth.testLoginAdmin
import com.openphonics.tests.Auth.validLoginAdmin
import com.openphonics.tests.Auth.validRegisterAdmin
import com.openphonics.tests.Flags.getFlagById
import com.openphonics.tests.Flags.getFlags
import com.openphonics.tests.Flags.invalidFlagRequestLongFlag
import com.openphonics.tests.Flags.invalidFlagRequestNumericFlag
import com.openphonics.tests.Flags.testCreateFlag
import com.openphonics.tests.Flags.validFlagRequest
import com.openphonics.tests.Languages.deleteLanguageById
import com.openphonics.tests.Languages.getLanguageById
import com.openphonics.tests.Languages.getLanguages
import com.openphonics.tests.Languages.invalidLanguageRequestLongLanguage
import com.openphonics.tests.Languages.invalidLanguageRequestLongName
import com.openphonics.tests.Languages.invalidLanguageRequestLongNative
import com.openphonics.tests.Languages.invalidLanguageRequestNumericLanguage
import com.openphonics.tests.Languages.invalidLanguageRequestNumericName
import com.openphonics.tests.Languages.invalidLanguageRequestNumericNative
import com.openphonics.tests.Languages.testCreateLanguage
import com.openphonics.tests.Languages.updateLanguageById
import com.openphonics.tests.Languages.validLanguageRequest
import com.openphonics.tests.Units.deleteUnitById
import com.openphonics.tests.Units.getUnitById
import com.openphonics.tests.Units.invalidUnitRequestInvalidLongTitle
import com.openphonics.tests.Units.invalidUnitRequestInvalidNegativeOrder
import com.openphonics.tests.Units.invalidUnitRequestInvalidNumericTitle
import com.openphonics.tests.Units.testCreateUnit
import com.openphonics.tests.Units.updateUnitById
import com.openphonics.tests.Units.validUnitRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

object Flags {
    val validFlagRequest = FlagRequest(
        VALID_FLAG_IMG,
        VALID_FLAG
    )
    val invalidFlagRequestLongFlag = FlagRequest(
        VALID_FLAG_IMG,
        INVALID_LONG_FLAG
    )
    val invalidFlagRequestNumericFlag = FlagRequest(
        VALID_FLAG_IMG,
        INVALID_NUMERIC_FLAG
    )
    const val FLAGS_URL = "${Routing.DATA}/${Routing.FLAGS}"
    val FLAG_BY_ID_URL: (String) -> String = { id: String ->"${Routing.DATA}/${Routing.FLAGS}/${id}"}
    const val CREATE_FLAG_URL = "${Routing.DATA}/${Routing.FLAGS}/${Routing.CREATE}"

    val createFlag: suspend (String, FlagRequest?, HttpClient) -> HttpResponse = { token, data, client ->
        client.post(CREATE_FLAG_URL) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val getFlags: suspend (HttpClient) -> HttpResponse = { client ->
        client.get(FLAGS_URL)
    }
    val getFlagById: suspend (String, HttpClient) -> HttpResponse = {id, client ->
        client.get(FLAG_BY_ID_URL(id))
    }
    val testCreateFlag: suspend(String?, FlagRequest?, HttpStatusCode, HttpClient) -> String = {tok, request, code, client ->
        val token = tok ?: testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
        val response = createFlag(token, request, client)
        assertEquals(code, response.status)
        val flag = extractResponse<StrIdResponse>(response)
        flag.id!!
    }
}
object Languages {

    const val LANGUAGES_URL = "${Routing.DATA}/${Routing.LANGUAGES}"
    val LANGUAGE_BY_ID_URL: (Int) -> String = {id: Int ->"${Routing.DATA}/${Routing.LANGUAGES}/${id}"}
    const val CREATE_LANGUAGE_URL = "${Routing.DATA}/${Routing.LANGUAGES}/${Routing.CREATE}"
    val languageRequest: (String, String, String) -> (String) -> LanguageRequest = { native, language, name ->
        {flag ->
            LanguageRequest(
                native,
                language,
                name,
                flag
            )
        }
    }
    val validLanguageRequest = languageRequest(
        VALID_NATIVE_ID,
        VALID_LANGUAGE_ID,
        VALID_LANGUAGE_NAME
    )
    val invalidLanguageRequestNumericNative = languageRequest(
        INVALID_NUMERIC_NATIVE_ID,
        VALID_LANGUAGE_ID,
        VALID_LANGUAGE_NAME
    )
    val invalidLanguageRequestLongNative = languageRequest(
        INVALID_LONG_NATIVE_ID,
        VALID_LANGUAGE_ID,
        VALID_LANGUAGE_NAME
    )
    val invalidLanguageRequestNumericLanguage = languageRequest(
        VALID_NATIVE_ID,
        INVALID_NUMERIC_LANGUAGE_ID,
        VALID_LANGUAGE_NAME
    )
    val invalidLanguageRequestLongLanguage = languageRequest(
        VALID_NATIVE_ID,
        INVALID_LONG_LANGUAGE_ID,
        VALID_LANGUAGE_NAME
    )
    val invalidLanguageRequestNumericName = languageRequest(
        VALID_NATIVE_ID,
        VALID_LANGUAGE_ID,
        INVALID_NUMERIC_LANGUAGE_NAME
    )
    val invalidLanguageRequestLongName = languageRequest(
        VALID_NATIVE_ID,
        VALID_LANGUAGE_ID,
        INVALID_LONG_LANGUAGE_NAME
    )


    val createLanguage: suspend (String, LanguageRequest?, HttpClient) -> HttpResponse = { token, data, client ->
        client.post(CREATE_LANGUAGE_URL) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val getLanguages: suspend (String, HttpClient, DepthRequest?) -> HttpResponse = { token, client, depth ->
        client.get(LANGUAGES_URL) {
            bearerAuth(token)
            setBody(depth)
        }
    }
    val getLanguageById: suspend (Int, HttpClient, DepthRequest?) -> HttpResponse = {id, client, depth ->
        client.get(LANGUAGE_BY_ID_URL(id)){
            setBody(depth)
        }
    }
    val deleteLanguageById: suspend (String, Int, HttpClient) -> HttpResponse = {token, id, client ->
        client.delete(LANGUAGE_BY_ID_URL(id)) {
            bearerAuth(token)
        }
    }
    val updateLanguageById: suspend (String, Int, LanguageRequest?, HttpClient) -> HttpResponse = {token, id,data, client ->
        client.put(LANGUAGE_BY_ID_URL(id)) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val testCreateLanguage: suspend(String?, (String)->LanguageRequest, HttpStatusCode, HttpClient) -> Int = {tok, request, code, client ->
        val token = tok ?: testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
        val flag = testCreateFlag(token, validFlagRequest, HttpStatusCode.OK, client)
        val response = createLanguage(token, request(flag), client )
        assertEquals(code, response.status)
        val language = extractResponse<IntIdResponse>(response)
        assertNotNull(language.id)
        language.id!!
    }

}

object Units {
    val UNIT_BY_ID_URL: (Int) -> String = {id: Int ->"${Routing.DATA}/${Routing.UNITS}/${id}"}
    const val CREATE_UNIT_URL = "${Routing.DATA}/${Routing.UNITS}/${Routing.CREATE}"

    val unitRequest: (String, Int) -> (Int) -> UnitRequest = { title, order ->
        {languageId ->
            UnitRequest(
                title,
                order,
                languageId
            )
        }
    }
    val validUnitRequest = unitRequest(
        VALID_TITLE,
        VALID_ORDER
    )
    val invalidUnitRequestInvalidLongTitle = unitRequest(
        INVALID_LONG_TITLE,
        VALID_ORDER
    )
    val invalidUnitRequestInvalidNumericTitle = unitRequest(
        INVALID_NUMERIC_TITLE,
        VALID_ORDER
    )
    val invalidUnitRequestInvalidNegativeOrder = unitRequest(
        VALID_TITLE,
        INVALID_ORDER
    )
    val createUnit: suspend (String, UnitRequest?, HttpClient) -> HttpResponse = { token, data, client ->
        client.post(CREATE_UNIT_URL) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val getUnitById: suspend (Int, HttpClient, DepthRequest?) -> HttpResponse = {id, client, depth ->
        client.get(UNIT_BY_ID_URL(id)){
            setBody(depth)
        }
    }
    val deleteUnitById: suspend (String, Int, HttpClient) -> HttpResponse = {token, id, client ->
        client.delete(UNIT_BY_ID_URL(id)) {
            bearerAuth(token)
        }
    }
    val updateUnitById: suspend (String, Int, UnitRequest?, HttpClient) -> HttpResponse = {token, id,data, client ->
        client.put(UNIT_BY_ID_URL(id)) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }

    val testCreateUnit: suspend(String?, (Int)->UnitRequest, HttpStatusCode, HttpClient) -> Int = {tok, request, code, client ->
        val token = tok ?: testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
        val language = testCreateLanguage(token, validLanguageRequest, HttpStatusCode.OK, client)
        val response = createUnit(token, request(language), client)
        assertEquals(code, response.status)
        val unit = extractResponse<IntIdResponse>(response)
        assertNotNull(unit.id)
        unit.id!!
    }
}
class DataTest {
    @Test
    fun testCreateUnitValid() = ApplicationTest.test() { client ->
       testCreateUnit(null, validUnitRequest, HttpStatusCode.OK, client)
    }
    @Test
    fun testCreateUnitInvalidLongTitle() = ApplicationTest.test() { client ->
        testCreateUnit(null, invalidUnitRequestInvalidLongTitle, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testCreateUnitInvalidNumericTitle() = ApplicationTest.test() { client ->
        testCreateUnit(null, invalidUnitRequestInvalidNumericTitle, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testCreateUnitInvalidOrder() = ApplicationTest.test() { client ->
        testCreateUnit(null, invalidUnitRequestInvalidNegativeOrder, HttpStatusCode.BadRequest, client)
    }

    @Test
    fun testGetUnit() = ApplicationTest.test() { client ->
        val unitId = testCreateUnit(null, validUnitRequest, HttpStatusCode.OK, client)
        val response = getUnitById(unitId, client, DepthRequest(0))
        assertEquals(HttpStatusCode.OK, response.status)
    }
    @Test
    fun testDeleteUnit() = ApplicationTest.test() { client ->
        val token = testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
        val unitId = testCreateUnit(token, validUnitRequest, HttpStatusCode.OK, client)
        val response = deleteUnitById(token, unitId, client)
        assertEquals(HttpStatusCode.OK, response.status)
    }
    @Test
    fun testUpdateUnit() = ApplicationTest.test() { client ->
        val token = testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
        val unitId = testCreateUnit(token, validUnitRequest, HttpStatusCode.OK, client)
        val unit = extractResponse<UnitResponse>(getUnitById(unitId, client, DepthRequest(0)))
        val response = updateUnitById(
            token,
            unitId,
            unit.unit.firstOrNull()?.let {
                UnitRequest(
                    "New Title",
                    VALID_ORDER,
                    it.language
                )
            },
            client
        )
        assertEquals(HttpStatusCode.OK, response.status)

    }
    @Test
    fun testCreateFlag() = ApplicationTest.test() { client ->
        testCreateFlag(null, validFlagRequest, HttpStatusCode.OK, client)
    }
    @Test
    fun testCreateFlagAlreadyExists() = ApplicationTest.test() { client ->
        val token = testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
        testCreateFlag(token, validFlagRequest, HttpStatusCode.OK, client)
        testCreateFlag(token, validFlagRequest, HttpStatusCode.OK, client)
    }
    @Test
    fun testCreateFlagInvalidLongId() = ApplicationTest.test() { client ->
        testCreateFlag(null, invalidFlagRequestLongFlag, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testCreateFlagInvalidNumericId() = ApplicationTest.test() { client ->
        testCreateFlag(null, invalidFlagRequestNumericFlag, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testGetAllFlags() = ApplicationTest.test() { client ->
        testCreateFlag(null, validFlagRequest, HttpStatusCode.OK, client)
        val response = getFlags(client)
        assertEquals(HttpStatusCode.OK, response.status)
    }
    @Test
    fun testGetFlag() = ApplicationTest.test() { client ->
        val flagId = testCreateFlag(null, validFlagRequest, HttpStatusCode.OK, client)
        val response = getFlagById(flagId, client)
        assertEquals(HttpStatusCode.OK, response.status)
    }
    @Test
    fun testCreateLanguage() = ApplicationTest.test() { client ->
        testCreateLanguage(null, validLanguageRequest, HttpStatusCode.OK, client)
    }
    @Test
    fun testCreateLanguageInvalidNumericNative() = ApplicationTest.test() { client ->
        testCreateLanguage(null, invalidLanguageRequestNumericNative, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testCreateLanguageInvalidLongNative() = ApplicationTest.test() { client ->
        testCreateLanguage(null, invalidLanguageRequestLongNative, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testCreateLanguageInvalidNumericLanguage() = ApplicationTest.test() { client ->
        testCreateLanguage(null, invalidLanguageRequestNumericLanguage, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testCreateLanguageInvalidLongLanguage() = ApplicationTest.test() { client ->
        testCreateLanguage(null, invalidLanguageRequestLongLanguage, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testCreateLanguageInvalidNumericName() = ApplicationTest.test() { client ->
        testCreateLanguage(null, invalidLanguageRequestNumericName, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testCreateLanguageInvalidLongName() = ApplicationTest.test() { client ->
        testCreateLanguage(null, invalidLanguageRequestLongName, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testCreateLanguageAlreadyExists() = ApplicationTest.test() { client ->
        val token = testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
        testCreateLanguage(token, validLanguageRequest, HttpStatusCode.OK, client)
        testCreateLanguage(token, validLanguageRequest, HttpStatusCode.BadRequest, client)
    }
    @Test
    fun testGetAllLanguages() = ApplicationTest.test() { client ->
        val token = testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
        testCreateLanguage(token, validLanguageRequest, HttpStatusCode.OK, client)
        val response = getLanguages(token, client, null)
        assertEquals(HttpStatusCode.OK, response.status)
    }
    @Test
    fun testGetLanguage() = ApplicationTest.test() { client ->
        val token = testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
        val languageId = testCreateLanguage(token, validLanguageRequest, HttpStatusCode.OK, client)
        val response = getLanguageById(languageId, client, null)
        assertEquals(HttpStatusCode.OK, response.status)
    }
    @Test
    fun testDeleteLanguage() = ApplicationTest.test() { client ->
        val token = testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
        val languageId = testCreateLanguage(token, validLanguageRequest, HttpStatusCode.OK, client)
        val response = deleteLanguageById(token, languageId, client)
        assertEquals(HttpStatusCode.OK, response.status)
    }
    @Test
    fun testUpdateLanguage() = ApplicationTest.test() { client ->
        val token = testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
        val languageId = testCreateLanguage(token, validLanguageRequest, HttpStatusCode.OK, client)
        val response = updateLanguageById(
            token,
            languageId,
            LanguageRequest(
                VALID_LANGUAGE_ID,
                VALID_NATIVE_ID,
                VALID_LANGUAGE_NAME,
                VALID_FLAG
            ),
            client
        )
        assertEquals(HttpStatusCode.OK, response.status)
    }
}