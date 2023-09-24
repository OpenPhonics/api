package com.openphonics.tests

import com.openphonics.ApplicationTest.Companion.extractResponse
import com.openphonics.Testing.INVALID_LESSON_COUNT
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
import com.openphonics.Testing.INVALID_PHONIC
import com.openphonics.Testing.INVALID_TRANSLATED_WORD
import com.openphonics.Testing.INVALID_WORD
import com.openphonics.Testing.VALID_FLAG
import com.openphonics.Testing.VALID_FLAG_IMG
import com.openphonics.Testing.VALID_LANGUAGE_ID
import com.openphonics.Testing.VALID_LANGUAGE_NAME
import com.openphonics.Testing.VALID_LESSON_COUNT
import com.openphonics.Testing.VALID_NATIVE_ID
import com.openphonics.Testing.VALID_ORDER
import com.openphonics.Testing.VALID_PHONIC
import com.openphonics.Testing.VALID_SOUND
import com.openphonics.Testing.VALID_TITLE
import com.openphonics.Testing.VALID_TRANSLATED_SOUND
import com.openphonics.Testing.VALID_TRANSLATED_WORD
import com.openphonics.Testing.VALID_WORD
import com.openphonics.model.request.*
import com.openphonics.model.response.*
import com.openphonics.route.data.Routing
import com.openphonics.tests.Auth.testLoginAdmin
import com.openphonics.tests.Auth.validLoginAdmin
import com.openphonics.tests.Auth.validRegisterAdmin
import com.openphonics.tests.Flags.testCreateFlag
import com.openphonics.tests.Flags.validFlagRequest
import com.openphonics.tests.Languages.testCreateLanguage
import com.openphonics.tests.Languages.validLanguageRequest
import com.openphonics.tests.Units.testCreateUnit
import com.openphonics.tests.Units.validUnitRequest
import com.openphonics.tests.Words.testCreateWord
import com.openphonics.tests.Words.wordRequest
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
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
    val getFlagById: suspend (String, String, HttpClient) -> HttpResponse = {token, id, client ->
        client.get(FLAG_BY_ID_URL(id)){
            bearerAuth(token)
        }
    }
    val deleteFlagById: suspend (String, String, HttpClient) -> HttpResponse = {token, id, client ->
        client.delete(FLAG_BY_ID_URL(id)) {
            bearerAuth(token)
        }
    }
    val updateFlagById: suspend (String, String, UpdateFlagRequest?, HttpClient) -> HttpResponse = {token,id, request, client ->
        client.put(FLAG_BY_ID_URL(id)) {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            setBody(request)
        }
    }
    data class Response(
        val flag: String,
        val token: String
    )
    val testCreateFlag: suspend(String?, FlagRequest?, HttpStatusCode, HttpClient) -> Response = { tok, request, code, client ->
        val token = tok ?: testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
        val response = createFlag(token, request, client)
        assertEquals(code, response.status)
        val flag = extractResponse<StrIdResponse>(response)
        assertNotNull(flag.id)
        Response(flag.id!!, token)
    }
}
object Languages {

    val LANGUAGES_URL: (String) -> String = {native: String ->"${Routing.DATA}/${Routing.LANGUAGES}/${Routing.ALL}/${native}"}
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
    data class Response(
        val flag: String,
        val token: String,
        val language: Int
    )
    val getLanguages: suspend (String, HttpClient, DepthRequest?) -> HttpResponse = { native, client, depth ->
        client.get(LANGUAGES_URL(native)) {
            contentType(ContentType.Application.Json)
            setBody(depth)
        }
    }
    val getLanguageById: suspend (String, Int, HttpClient, DepthRequest?) -> HttpResponse = { token, id, client, depth ->
        client.get(LANGUAGE_BY_ID_URL(id)){
            contentType(ContentType.Application.Json)
            setBody(depth)
            bearerAuth(token)
        }
    }
    val deleteLanguageById: suspend (String, Int, HttpClient) -> HttpResponse = {token, id, client ->
        client.delete(LANGUAGE_BY_ID_URL(id)) {
            bearerAuth(token)
        }
    }
    val updateLanguageById: suspend (String, Int, LanguageRequest?, HttpClient) -> HttpResponse = { token, id, data, client ->
        client.put(LANGUAGE_BY_ID_URL(id)) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val testCreateLanguage: suspend(Flags.Response?, (String)-> LanguageRequest, HttpStatusCode, HttpClient) -> Response = { cred, request, code, client ->
        val creds = cred?: testCreateFlag(null, validFlagRequest, HttpStatusCode.OK, client)
        val response = createLanguage(creds.token, request(creds.flag), client )
        assertEquals(code, response.status)
        val language = extractResponse<IntIdResponse>(response)
        assertNotNull(language.id)
        Response(creds.flag, creds.token,language.id!!)
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
    data class Response(
        val language: Int,
        val token: String,
        val unit: Int
    )
    val createUnit: suspend (String, UnitRequest?, HttpClient) -> HttpResponse = { token, data, client ->
        client.post(CREATE_UNIT_URL) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val getUnitById: suspend (String, Int, HttpClient, DepthRequest?) -> HttpResponse = {token, id, client, depth ->
        client.get(UNIT_BY_ID_URL(id)){
            contentType(ContentType.Application.Json)
            setBody(depth)
            bearerAuth(token)
        }
    }
    val deleteUnitById: suspend (String, Int, HttpClient) -> HttpResponse = {token, id, client ->
        client.delete(UNIT_BY_ID_URL(id)) {
            bearerAuth(token)
        }
    }
    val updateUnitById: suspend (String, Int, UpdateUnitRequest?, HttpClient) -> HttpResponse = { token, id, data, client ->
        client.put(UNIT_BY_ID_URL(id)) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val testCreateUnit: suspend(Languages.Response?, (Int)-> UnitRequest, HttpStatusCode, HttpClient) -> Response = { cred, request, code, client ->
        val creds = cred?: testCreateLanguage(null, validLanguageRequest, HttpStatusCode.OK, client)
        val response = createUnit(creds.token, request(creds.language), client)
        assertEquals(code, response.status)
        val unit = extractResponse<IntIdResponse>(response)
        assertNotNull(unit.id)
        Response(creds.language, creds.token, unit.id!!)

    }
//    val checkUnitOrder: suspend (Map<Int,Int>, HttpClient) -> Unit = {unitIds, client->
//        unitIds.forEach { (id, order) ->
//            val unit = extractResponse<UnitResponse>(getUnitById(id, client, null)).unit.firstOrNull()
//            assertNotNull(unit)
//            assertEquals(unit.order, order)
//        }
//    }
//    val reOrderUnits: suspend (String, MutableMap<Int,Int>, Int, Int, HttpClient, Int) -> Unit = { token, unitIds, from, to, client, languageId ->
//        unitIds.forEach {(id, order) ->
//            if (order == from){
//                updateUnitById(token,id, unitRequest(VALID_TITLE, to)(languageId), client)
//                unitIds[id] = to
//            } else {
//                if (order > from) {
//                    unitIds[id] = order-1
//                }
//                if (order >= to){
//                    unitIds[id] = unitIds[id]!! + 1
//                }
//            }
//        }
//    }

}
object Sections {
    val SECTION_BY_ID_URL: (Int) -> String = {id: Int ->"${Routing.DATA}/${Routing.SECTIONS}/${id}"}
    const val CREATE_SECTION_URL = "${Routing.DATA}/${Routing.SECTIONS}/${Routing.CREATE}"
    val ADD_WORD_TO_SECTION_URL: (Int) -> String = {id: Int ->"${Routing.DATA}/${Routing.SECTIONS}/${id}/${Routing.WORD}"}
    val ADD_SENTENCE_TO_SECTION_URL: (Int) -> String = {id: Int ->"${Routing.DATA}/${Routing.SECTIONS}/${id}/${Routing.SENTENCE}"}
    val sectionRequest: (String, Int, Int) -> (Int) -> SectionRequest = { title, order, lessonCount ->
        {unitId ->
            SectionRequest(
                title,
                order,
                lessonCount,
                unitId
            )
        }
    }
    val validSectionRequest = sectionRequest(
        VALID_TITLE,
        VALID_ORDER,
        VALID_LESSON_COUNT
    )
    val invalidSectionRequestInvalidLongTitle = sectionRequest(
        INVALID_LONG_TITLE,
        VALID_ORDER,
        VALID_LESSON_COUNT
    )
    val invalidSectionRequestInvalidNumericTitle = sectionRequest(
        INVALID_NUMERIC_TITLE,
        VALID_ORDER,
        VALID_LESSON_COUNT
    )
    val invalidSectionRequestInvalidNegativeOrder = sectionRequest(
        VALID_TITLE,
        INVALID_ORDER,
        VALID_LESSON_COUNT
    )
    val invalidSectionRequestInvalidLessonCount = sectionRequest(
        VALID_TITLE,
        VALID_ORDER,
        INVALID_LESSON_COUNT
    )
    data class Response(
        val token: String,
        val unit: Int,
        val section: Int
    )
    val postWordsToSection: suspend (String, Int, WordSectionRequest?, HttpClient) -> HttpResponse = { token, sectionId, data, client ->
        client.post(ADD_WORD_TO_SECTION_URL(sectionId)) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val postSentencesToSection: suspend (String, Int, SentenceSectionRequest?, HttpClient) -> HttpResponse = { token, sectionId, data, client ->
        client.post(ADD_SENTENCE_TO_SECTION_URL(sectionId)) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val deleteWordsToSection: suspend (String, Int, WordSectionRequest?, HttpClient) -> HttpResponse = { token, sectionId, data, client ->
        client.delete(ADD_WORD_TO_SECTION_URL(sectionId)) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val deleteSentencesToSection: suspend (String, Int, SentenceSectionRequest?, HttpClient) -> HttpResponse = { token, sectionId, data, client ->
        client.delete(ADD_SENTENCE_TO_SECTION_URL(sectionId)) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val createSection: suspend (String, SectionRequest?, HttpClient) -> HttpResponse = { token, data, client ->
        client.post(CREATE_SECTION_URL) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val getSectionById: suspend (String, Int, HttpClient, DepthRequest?) -> HttpResponse = { token, id, client, depth ->
        client.get(SECTION_BY_ID_URL(id)){
            contentType(ContentType.Application.Json)
            setBody(depth)
            bearerAuth(token)
        }
    }
    val deleteSectionById: suspend (String, Int, HttpClient) -> HttpResponse = {token, id, client ->
        client.delete(SECTION_BY_ID_URL(id)) {
            bearerAuth(token)
        }
    }
    val updateSectionById: suspend (String, Int, SectionRequest?, HttpClient) -> HttpResponse = { token, id, data, client ->
        client.put(SECTION_BY_ID_URL(id)) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val testCreateSection: suspend(Units.Response?, (Int)-> SectionRequest, HttpStatusCode, HttpClient) -> Response = { cred, request, code, client ->
        val creds = cred ?: testCreateUnit(null, validUnitRequest, HttpStatusCode.OK, client)
        val response = createSection(creds.token, request(creds.unit), client)
        assertEquals(code, response.status)
        val section = extractResponse<IntIdResponse>(response)
        assertNotNull(section.id)
        Response(creds.token, creds.unit, section.id!!)
    }
    val testAddWords: suspend (Response?, WordSectionRequest, HttpStatusCode, HttpClient) -> Unit = { cred, request, code, client ->
        val creds = cred ?: testCreateSection(null, validSectionRequest, HttpStatusCode.OK, client)
        val response = postWordsToSection(creds.token, creds.section, request, client)
        assertEquals(code, response.status)
    }
    val testAddSentences: suspend (Response?, SentenceSectionRequest, HttpStatusCode, HttpClient) -> Unit = { cred, request, code, client ->
        val creds = cred ?: testCreateSection(null, validSectionRequest, HttpStatusCode.OK, client)
        val response = postSentencesToSection(creds.token, creds.section, request, client)
        assertEquals(code, response.status)
    }
    val testRemoveWords: suspend (Response?, WordSectionRequest, HttpStatusCode, HttpClient) -> Unit = { cred, request, code, client ->
        val creds = cred ?: testCreateSection(null, validSectionRequest, HttpStatusCode.OK, client)
        val response = deleteWordsToSection(creds.token, creds.section, request, client)
        assertEquals(code, response.status)
    }
    val testRemoveSentences: suspend (Response?, SentenceSectionRequest, HttpStatusCode, HttpClient) -> Unit = { cred, request, code, client ->
        val creds = cred ?: testCreateSection(null, validSectionRequest, HttpStatusCode.OK, client)
        val response = deleteSentencesToSection(creds.token, creds.section, request, client)
        assertEquals(code, response.status)
    }
//    val checkSectionOrder: suspend (Map<Int,Int>, HttpClient) -> Unit = {sectionIds, client->
//        sectionIds.forEach { (id, order) ->
//            val section = extractResponse<UnitResponse>(getSectionById(id, client, null)).unit.firstOrNull()
//            assertNotNull(section)
//            assertEquals(section.order, order)
//        }
//    }
    val reOrderSections: suspend (String, MutableMap<Int,Int>, Int, Int, HttpClient, Int) -> Unit = { token, sectionIds, from, to, client, unitId ->
        sectionIds.forEach {(id, order) ->
            if (order == from){
                updateSectionById(token,id, sectionRequest(VALID_TITLE, to, VALID_LESSON_COUNT)(unitId), client)
                sectionIds[id] = to
            } else {
                if (order > from) {
                    sectionIds[id] = order-1
                }
                if (order >= to){
                    sectionIds[id] = sectionIds[id]!! + 1
                }
            }
        }
    }

}
object Words {
    val WORD_BY_ID_URL: (Int) -> String = {id: Int ->"${Routing.DATA}/${Routing.WORDS}/${id}"}
    const val CREATE_WORD_URL = "${Routing.DATA}/${Routing.WORDS}/${Routing.CREATE}"
    val wordRequest: (String, String, String, String, String) -> (Int) -> WordRequest = { phonic, sound, translatedSound, translatedWord, word ->
        {language ->
            WordRequest(
                phonic,
                sound,
                translatedSound,
                translatedWord,
                word,
                language
            )
        }
    }
    val validWordRequest = wordRequest(
        VALID_PHONIC,
        VALID_SOUND,
        VALID_TRANSLATED_SOUND,
        VALID_TRANSLATED_WORD,
        VALID_WORD
    )
    val invalidWordRequestInvalidPhonic = wordRequest(
        INVALID_PHONIC,
        VALID_SOUND,
        VALID_TRANSLATED_SOUND,
        VALID_TRANSLATED_WORD,
        VALID_WORD
    )
    val invalidWordRequestInvalidWord = wordRequest(
        VALID_PHONIC,
        VALID_SOUND,
        VALID_TRANSLATED_SOUND,
        VALID_TRANSLATED_WORD,
        INVALID_WORD
    )
    val invalidWordRequestInvalidTranslatedWord = wordRequest(
        VALID_PHONIC,
        VALID_SOUND,
        VALID_TRANSLATED_SOUND,
        INVALID_TRANSLATED_WORD,
        VALID_WORD
    )
    data class Response(
        val token: String,
        val language: Int,
        val word: Int
    )
    val createWord: suspend (String, WordRequest?, HttpClient) -> HttpResponse = { token, data, client ->
        client.post(CREATE_WORD_URL) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val getWordById: suspend (String, Int, HttpClient) -> HttpResponse = {token, id, client->
        client.get(WORD_BY_ID_URL(id)){
            bearerAuth(token)
        }
    }
    val deleteWordById: suspend (String, Int, HttpClient) -> HttpResponse = {token, id, client ->
        client.delete(WORD_BY_ID_URL(id)) {
            bearerAuth(token)
        }
    }
    val updateWordById: suspend (String, Int, UpdateWordRequest?, HttpClient) -> HttpResponse = { token, id, data, client ->
        client.put(WORD_BY_ID_URL(id)) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val testCreateWord: suspend(Languages.Response?, (Int) -> WordRequest, HttpStatusCode, HttpClient) -> Response = { cred, request, code, client ->
        val creds = cred?: testCreateLanguage(null, validLanguageRequest, HttpStatusCode.OK, client)
        val response = createWord(creds.token, request(creds.language), client)
        assertEquals(code, response.status)
        val word = extractResponse<IntIdResponse>(response)
        assertNotNull(word.id)
        Response(creds.token, creds.language, word.id!!)
    }

}
object Sentences {
    val SENTENCE_BY_ID_URL: (Int) -> String = {id: Int ->"${Routing.DATA}/${Routing.SENTENCES}/${id}"}
    const val CREATE_SENTENCE_URL = "${Routing.DATA}/${Routing.SENTENCES}/${Routing.CREATE}"
    val sentenceRequest: (List<Int>, Int) -> SentenceRequest = { words, language ->
        SentenceRequest(
            language,
            words
        )
    }
    data class Response(
        val token: String,
        val language: Int,
        val sentence: Int
    )
    val createSentence: suspend (String, SentenceRequest?, HttpClient) -> HttpResponse = { token, data, client ->
        client.post(CREATE_SENTENCE_URL) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val getSentenceById: suspend (String, Int, HttpClient) -> HttpResponse = {token, id, client->
        client.get(SENTENCE_BY_ID_URL(id)){
            bearerAuth(token)
        }
    }
    val deleteSentenceById: suspend (String, Int, HttpClient) -> HttpResponse = {token, id, client ->
        client.delete(SENTENCE_BY_ID_URL(id)) {
            bearerAuth(token)
        }
    }
    val updateSentenceById: suspend (String, Int, UpdateSentenceRequest?, HttpClient) -> HttpResponse = { token, id, data, client ->
        client.put(SENTENCE_BY_ID_URL(id)) {
            contentType(ContentType.Application.Json)
            setBody(data)
            bearerAuth(token)
        }
    }
    val testCreateSentence: suspend(Languages.Response?, (List<Int>, Int) -> SentenceRequest, HttpStatusCode, HttpClient) -> Response = { cred, request, code, client ->
        val creds = cred?: testCreateLanguage(null, validLanguageRequest, HttpStatusCode.OK, client)
        val validChars = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k")
        val validWords = validChars.map {
            wordRequest(
                it,
                it,
                it,
                it,
                it
            )
        }
        val ids = validWords.map {word ->
            testCreateWord(creds, word, HttpStatusCode.OK, client).word
        }
        val response = createSentence(creds.token, request(ids, creds.language), client)
        assertEquals(code, response.status)
        val sentence = extractResponse<IntIdResponse>(response)
        assertNotNull(sentence.id)
        Response(creds.token, creds.language, sentence.id!!)
    }

}


//class DataTest {
//    @Test
//    fun testDepth() = ApplicationTest.test() {client ->
//        val credLanguage = testCreateLanguage(null, validLanguageRequest, HttpStatusCode.OK, client)
//        val words: MutableList<Int> = mutableListOf()
//        val sentences: MutableList<Int> = mutableListOf()
//        val units: MutableList<Int> = mutableListOf()
//        val sections: MutableList<Int> = mutableListOf()
//        for (i in 0..10){
//            val word = wordRequest(
//                VALID_PHONIC,
//                VALID_SOUND,
//                VALID_TRANSLATED_SOUND,
//                VALID_TRANSLATED_WORD,
//                ('a'.code +i).digitToChar().toString(),
//            )
//            words.add(testCreateWord(credLanguage, word, HttpStatusCode.OK, client).word)
//            val unitCred = testCreateUnit(credLanguage, validUnitRequest, HttpStatusCode.OK, client)
//            units.add(unitCred.unit)
//            for (k in 0..10){
//                val sentence = SentenceRequest(
//                    credLanguage.language,
//                    words
//                )
//                val id = extractResponse<IntIdResponse>(createSentence(credLanguage.token, sentence, client)).id
//                assertNotNull(id)
//                sentences.add(id)
//            }
//            for (j in 0..10){
//                val sectionCred = testCreateSection(unitCred, validSectionRequest, HttpStatusCode.OK, client)
//                sections.add(sectionCred.section)
//                testAddWords(sectionCred, WordSectionRequest(words), HttpStatusCode.OK, client)
//                testAddSentences(sectionCred, SentenceSectionRequest(sentences), HttpStatusCode.OK, client)
//            }
//        }
//        val languageDepthOne = getLanguageById(credLanguage.language, client, DepthRequest(Language.LANGUAGE))
//        assertEquals(HttpStatusCode.OK, languageDepthOne.status)
//        val languageDataDepthOne = extractResponse<LanguageResponse>(languageDepthOne).language[0]
//        assertEquals(false, languageDataDepthOne.hasData)
//        assertEquals(emptyList(), languageDataDepthOne.units)
//
//        val languageDepthTwo = getLanguageById(credLanguage.language, client, DepthRequest(Language.LANGUAGE_WITH_UNITS))
//        assertEquals(HttpStatusCode.OK, languageDepthTwo.status)
//        val languageDataDepthTwo = extractResponse<LanguageResponse>(languageDepthTwo).language[0]
//        assertEquals(true, languageDataDepthTwo.hasData)
//        assertEquals(11, languageDataDepthTwo.units.size)
//        languageDataDepthTwo.units.forEach {
//            assertEquals(false, it.hasData)
//        }
//
//        val languageDepthThree = getLanguageById(credLanguage.language, client, DepthRequest(Language.LANGUAGE_WITH_UNITS_WITH_SECTIONS))
//        assertEquals(HttpStatusCode.OK, languageDepthThree.status)
//        val languageDataDepthThree = extractResponse<LanguageResponse>(languageDepthThree).language[0]
//        assertEquals(true, languageDataDepthThree.hasData)
//        assertEquals(11, languageDataDepthThree.units.size)
//        languageDataDepthThree.units.forEach {unit->
//            assertEquals(true, unit.hasData)
//            assertEquals(11, unit.sections.size)
//            unit.sections.forEach {section ->
//                assertEquals(false, section.hasData)
//            }
//        }
//
//        val languageDepthFour = getLanguageById(credLanguage.language, client, DepthRequest(Language.LANGUAGE_WITH_UNITS_WITH_SECTION_WITH_LESSON_DATA))
//        assertEquals(HttpStatusCode.OK, languageDepthFour.status)
//        val languageDataDepthFour = extractResponse<LanguageResponse>(languageDepthFour).language[0]
//        assertEquals(true, languageDataDepthFour.hasData)
//        assertEquals(11, languageDataDepthFour.units.size)
//        languageDataDepthFour.units.forEach {unit->
//            assertEquals(true, unit.hasData)
//            assertEquals(11, unit.sections.size)
//            unit.sections.forEach {section ->
//                assertEquals(true, section.hasData)
//                assertEquals(11, section.sentences.size)
//                assertEquals(11, section.words.size)
//            }
//        }
//
//        units.forEach { unit ->
//            val unitDepthOne = getUnitById(unit, client, DepthRequest(com.openphonics.data.model.data.Unit.UNIT))
//            assertEquals(HttpStatusCode.OK, languageDepthOne.status)
//            val unitDataDepthOne = extractResponse<UnitResponse>(unitDepthOne).unit[0]
//            assertEquals(false, unitDataDepthOne.hasData)
//            assertEquals(emptyList(), unitDataDepthOne.sections)
//
//            val unitDepthTwo = getUnitById(unit, client, DepthRequest(com.openphonics.data.model.data.Unit.UNIT_WITH_SECTIONS))
//            assertEquals(HttpStatusCode.OK, unitDepthTwo.status)
//            val unitDataDepthTwo = extractResponse<UnitResponse>(unitDepthTwo).unit[0]
//            assertEquals(true, unitDataDepthTwo.hasData)
//            assertEquals(11, unitDataDepthTwo.sections.size)
//            unitDataDepthTwo.sections.forEach {
//                assertEquals(false, it.hasData)
//            }
//
//            val unitDepthThree = getUnitById(unit, client, DepthRequest(com.openphonics.data.model.data.Unit.UNIT_WITH_SECTIONS_WITH_LESSON_DATA))
//            assertEquals(HttpStatusCode.OK, unitDepthThree.status)
//            val unitDataDepthThree = extractResponse<UnitResponse>(unitDepthThree).unit[0]
//            assertEquals(true, unitDataDepthThree.hasData)
//            assertEquals(11, unitDataDepthThree.sections.size)
//            unitDataDepthThree.sections.forEach {section->
//                assertEquals(true, section.hasData)
//                assertEquals(11, section.sentences.size)
//                assertEquals(11, section.words.size)
//            }
//        }
//
//        sections.forEach { section ->
//            val sectionDepthOne = getSectionById(section, client, DepthRequest(Section.SECTION))
//            assertEquals(HttpStatusCode.OK, sectionDepthOne.status)
//            val sectionDataDepthOne = extractResponse<SectionResponse>(sectionDepthOne).section[0]
//            assertEquals(false, sectionDataDepthOne.hasData)
//            assertEquals(emptyList(), sectionDataDepthOne.sentences)
//            assertEquals(emptyList(), sectionDataDepthOne.words)
//
//            val sectionDepthTwo = getSectionById(section, client, DepthRequest(Section.SECTIONS_WITH_LESSON_DATA))
//            assertEquals(HttpStatusCode.OK, sectionDepthTwo.status)
//            val sectionDataDepthTwo = extractResponse<SectionResponse>(sectionDepthTwo).section[0]
//            assertEquals(true, sectionDataDepthTwo.hasData)
//            assertEquals(11, sectionDataDepthTwo.sentences.size)
//            assertEquals(11, sectionDataDepthTwo.words.size)
//
//        }
//    }
//    @Test
//    fun testCreateSentenceValid() = ApplicationTest.test() {client->
//        testCreateSentence(null, sentenceRequest, HttpStatusCode.OK, client)
//    }
//    @Test
//    fun testUpdateSentenceValid() = ApplicationTest.test() { client ->
//        val cred = testCreateSentence(null, sentenceRequest, HttpStatusCode.OK, client)
//        val response = updateSentenceById(cred.token, cred.sentence, SentenceRequest(cred.language, emptyList()), client)
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testDeleteSentenceValid() = ApplicationTest.test() { client ->
//        val cred = testCreateSentence(null, sentenceRequest, HttpStatusCode.OK, client)
//        val response = deleteSentenceById(cred.token, cred.sentence, client)
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testGetSentenceValid() = ApplicationTest.test() { client ->
//        val cred = testCreateSentence(null, sentenceRequest, HttpStatusCode.OK, client)
//        val response = getSentenceById(cred.sentence, client)
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testCreateWordValid() = ApplicationTest.test() {client->
//        testCreateWord(null, validWordRequest, HttpStatusCode.OK, client)
//    }
//
//    @Test
//    fun testUpdateWordValid() = ApplicationTest.test() { client ->
//        val cred = testCreateWord(null, validWordRequest, HttpStatusCode.OK, client)
//        val response = updateWordById(cred.token, cred.word, validWordRequest(cred.language), client)
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testDeleteWordValid() = ApplicationTest.test() { client ->
//        val cred = testCreateWord(null, validWordRequest, HttpStatusCode.OK, client)
//        val response = deleteWordById(cred.token, cred.word, client)
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testGetWordValid() = ApplicationTest.test() { client ->
//        val cred = testCreateWord(null, validWordRequest, HttpStatusCode.OK, client)
//        val response = getWordById(cred.word, client)
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testCreateWordInValidPhonic() = ApplicationTest.test() {client->
//        testCreateWord(null, invalidWordRequestInvalidPhonic, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateWordInValidWord() = ApplicationTest.test() {client->
//        testCreateWord(null, invalidWordRequestInvalidWord, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateWordInValidTranslatedWord() = ApplicationTest.test() {client->
//        testCreateWord(null, invalidWordRequestInvalidTranslatedWord, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateWordInvalidAlreadyExists() = ApplicationTest.test() {client->
//        val response = testCreateLanguage(null, validLanguageRequest, HttpStatusCode.OK, client)
//        testCreateWord(response, validWordRequest, HttpStatusCode.OK, client)
//        testCreateWord(response, validWordRequest, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateUnitValid() = ApplicationTest.test() { client ->
//        testCreateUnit(null, validUnitRequest, HttpStatusCode.OK, client)
//    }
//    @Test
//    fun testCreateUnitInvalidLongTitle() = ApplicationTest.test() { client ->
//        testCreateUnit(null, invalidUnitRequestInvalidLongTitle, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateUnitInvalidNumericTitle() = ApplicationTest.test() { client ->
//        testCreateUnit(null, invalidUnitRequestInvalidNumericTitle, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateUnitInvalidOrder() = ApplicationTest.test() { client ->
//        testCreateUnit(null, invalidUnitRequestInvalidNegativeOrder, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testUnitOrdering() = ApplicationTest.test() {client ->
//        val response = testCreateLanguage(null, validLanguageRequest, HttpStatusCode.OK, client)
//        val token = response.token
//        val languageId = response.language
//        val unitIds: MutableMap<Int, Int> = mutableMapOf()
//        val minOrder = 0
//        val maxOrder = 9
//        for (i in minOrder..maxOrder){
//            val unitId = extractResponse<IntIdResponse>(createUnit(token, unitRequest(VALID_TITLE, VALID_ORDER)(languageId), client)).id
//            assertNotNull(unitId)
//            unitIds[unitId] = maxOrder-i
//        }
//        checkUnitOrder(unitIds, client)
//        val orderPairs = mapOf(
//            2 to 4,
//            3 to 3,
//            4 to 2,
//            0 to 6,
//            9 to 5,
//            3 to 10
//        )
//        orderPairs.forEach {(from, to)->
//            reOrderUnits(token,unitIds,from, to, client, languageId)
//            checkUnitOrder(unitIds, client)
//        }
//        val insertVals = listOf(9, 12, 5, 0)
//        insertVals.forEach {insert ->
//            val unitId = extractResponse<IntIdResponse>(createUnit(token, unitRequest(VALID_TITLE, insert)(languageId), client)).id
//            assertNotNull(unitId)
//            unitIds.forEach {(id, order)->
//                if (order >= insert){
//                    unitIds[id] = unitIds[id]!! + 1
//                }
//            }
//            unitIds[unitId] = insert
//            checkUnitOrder(unitIds, client)
//        }
//    }
//    @Test
//    fun testGetUnit() = ApplicationTest.test() { client ->
//        val unitId = testCreateUnit(null, validUnitRequest, HttpStatusCode.OK, client).unit
//        val response = getUnitById(unitId, client, DepthRequest(0))
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testDeleteUnit() = ApplicationTest.test() { client ->
//        val cred = testCreateUnit(null, validUnitRequest, HttpStatusCode.OK, client)
//        val response = deleteUnitById(cred.token, cred.unit, client)
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testUpdateUnit() = ApplicationTest.test() { client ->
//        val cred = testCreateUnit(null, validUnitRequest, HttpStatusCode.OK, client)
//        val unit = extractResponse<UnitResponse>(getUnitById(cred.unit, client, DepthRequest(0)))
//        val response = updateUnitById(
//            cred.token,
//            cred.unit,
//            unit.unit.firstOrNull()?.let {
//                UnitRequest(
//                    "New Title",
//                    VALID_ORDER,
//                    it.language
//                )
//            },
//            client
//        )
//        assertEquals(HttpStatusCode.OK, response.status)
//
//    }
//    @Test
//    fun testCreateFlag() = ApplicationTest.test() { client ->
//        testCreateFlag(null, validFlagRequest, io.ktor.http.HttpStatusCode.OK, client)
//    }
//    @Test
//    fun testCreateFlagAlreadyExists() = ApplicationTest.test() { client ->
//        val token = testLoginAdmin(validRegisterAdmin, validLoginAdmin, HttpStatusCode.OK, client)
//        testCreateFlag(token, validFlagRequest, HttpStatusCode.OK, client)
//        testCreateFlag(token, validFlagRequest, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateFlagInvalidLongId() = ApplicationTest.test() { client ->
//        testCreateFlag(null, invalidFlagRequestLongFlag, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateFlagInvalidNumericId() = ApplicationTest.test() { client ->
//        testCreateFlag(null, invalidFlagRequestNumericFlag, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testGetAllFlags() = ApplicationTest.test() { client ->
//        testCreateFlag(null, validFlagRequest, HttpStatusCode.OK, client)
//        val response = getFlags(client)
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testGetFlag() = ApplicationTest.test() { client ->
//        val flagId = testCreateFlag(null, validFlagRequest, HttpStatusCode.OK, client).flag
//        val response = getFlagById(flagId, client)
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testCreateLanguage() = ApplicationTest.test() { client ->
//        testCreateLanguage(null, validLanguageRequest, io.ktor.http.HttpStatusCode.OK, client)
//    }
//    @Test
//    fun testCreateLanguageInvalidNumericNative() = ApplicationTest.test() { client ->
//        testCreateLanguage(null, invalidLanguageRequestNumericNative, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateLanguageInvalidLongNative() = ApplicationTest.test() { client ->
//        testCreateLanguage(null, invalidLanguageRequestLongNative, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateLanguageInvalidNumericLanguage() = ApplicationTest.test() { client ->
//        testCreateLanguage(null, invalidLanguageRequestNumericLanguage, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateLanguageInvalidLongLanguage() = ApplicationTest.test() { client ->
//        testCreateLanguage(null, invalidLanguageRequestLongLanguage, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateLanguageInvalidNumericName() = ApplicationTest.test() { client ->
//        testCreateLanguage(null, invalidLanguageRequestNumericName, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateLanguageInvalidLongName() = ApplicationTest.test() { client ->
//        testCreateLanguage(null, invalidLanguageRequestLongName, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateLanguageAlreadyExists() = ApplicationTest.test() { client ->
//        val token = testCreateFlag(null, validFlagRequest, HttpStatusCode.OK, client)
//        testCreateLanguage(token, validLanguageRequest, HttpStatusCode.OK, client)
//        testCreateLanguage(token, validLanguageRequest, HttpStatusCode.OK, client)
//    }
//    @Test
//    fun testGetAllLanguages() = ApplicationTest.test() { client ->
//        val cred = testCreateLanguage(null, validLanguageRequest, HttpStatusCode.OK, client)
//        val response = getLanguages(cred.token, client, null)
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testGetLanguage() = ApplicationTest.test() { client ->
//        val cred = testCreateLanguage(null, validLanguageRequest, HttpStatusCode.OK, client)
//        val response = getLanguageById(cred.language, client, null)
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testDeleteLanguage() = ApplicationTest.test() { client ->
//        val cred= testCreateLanguage(null, validLanguageRequest, HttpStatusCode.OK, client)
//        val response = deleteLanguageById(cred.token, cred.language, client)
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testUpdateLanguage() = ApplicationTest.test() { client ->
//        val cred = testCreateLanguage(null, validLanguageRequest, HttpStatusCode.OK, client)
//        val response = updateLanguageById(
//            cred.token,
//            cred.language,
//            LanguageRequest(
//                VALID_LANGUAGE_ID,
//                VALID_NATIVE_ID,
//                VALID_LANGUAGE_NAME,
//                VALID_FLAG
//            ),
//            client
//        )
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testCreateSectionValid() = ApplicationTest.test() { client ->
//        testCreateSection(null, validSectionRequest, HttpStatusCode.OK, client)
//    }
//    @Test
//    fun testCreateSectionInvalidLessonCount() = ApplicationTest.test() { client ->
//        testCreateSection(null, invalidSectionRequestInvalidLessonCount, HttpStatusCode.OK, client)
//    }
//    @Test
//    fun testCreateSectionInvalidLongTitle() = ApplicationTest.test() { client ->
//        testCreateSection(null, invalidSectionRequestInvalidLongTitle, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateSectionInvalidNumericTitle() = ApplicationTest.test() { client ->
//        testCreateSection(null, invalidSectionRequestInvalidNumericTitle, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testCreateSectionInvalidOrder() = ApplicationTest.test() { client ->
//        testCreateSection(null, invalidSectionRequestInvalidNegativeOrder, HttpStatusCode.BadRequest, client)
//    }
//    @Test
//    fun testSectionOrdering() = ApplicationTest.test() {client ->
//        val cred = testCreateUnit(null, validUnitRequest, HttpStatusCode.OK, client)
//        val sectionIds: MutableMap<Int, Int> = mutableMapOf()
//        val minOrder = 0
//        val maxOrder = 9
//        for (i in minOrder..maxOrder){
//            val sectionId = extractResponse<IntIdResponse>(createSection(cred.token, sectionRequest(VALID_TITLE, VALID_ORDER, VALID_LESSON_COUNT)(cred.unit), client)).id
//            assertNotNull(sectionId)
//            sectionIds[sectionId] = maxOrder-i
//        }
//        checkSectionOrder(sectionIds, client)
//        val orderPairs = mapOf(
//            2 to 4,
//            3 to 3,
//            4 to 2,
//            0 to 6,
//            9 to 5,
//            3 to 10
//        )
//        orderPairs.forEach {(from, to)->
//            reOrderSections(cred.token,sectionIds,from, to, client, cred.unit)
//            checkSectionOrder(sectionIds, client)
//        }
//        val insertVals = listOf(9, 12, 5, 0)
//        insertVals.forEach {insert ->
//            val sectionId = extractResponse<IntIdResponse>(createSection(cred.token, sectionRequest(VALID_TITLE, insert, VALID_LESSON_COUNT)(cred.unit), client)).id
//            assertNotNull(sectionId)
//            sectionIds.forEach {(id, order)->
//                if (order >= insert){
//                    sectionIds[id] = sectionIds[id]!! + 1
//                }
//            }
//            sectionIds[sectionId] = insert
//            checkSectionOrder(sectionIds, client)
//        }
//    }
//    @Test
//    fun testGetSection() = ApplicationTest.test() { client ->
//        val sectionId = testCreateSection(null, validSectionRequest, HttpStatusCode.OK, client).section
//        val response = getSectionById(sectionId, client, null)
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testDeleteSection() = ApplicationTest.test() { client ->
//        val cred = testCreateSection(null, validSectionRequest, HttpStatusCode.OK, client)
//        val response = deleteSectionById(cred.token, cred.section, client)
//        assertEquals(HttpStatusCode.OK, response.status)
//    }
//    @Test
//    fun testUpdateSection() = ApplicationTest.test() { client ->
//        val cred = testCreateSection(null, validSectionRequest, HttpStatusCode.OK, client)
//        val section = extractResponse<SectionResponse>(getSectionById(cred.section, client, DepthRequest(0)))
//        val response = updateSectionById(
//            cred.token,
//            cred.section,
//            section.section.firstOrNull()?.let {
//                SectionRequest(
//                    "New Title",
//                    VALID_ORDER,
//                    VALID_LESSON_COUNT,
//                    it.unit
//                )
//            },
//            client
//        )
//        assertEquals(HttpStatusCode.OK, response.status)
//
//    }
//}