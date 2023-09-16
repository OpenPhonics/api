package com.openphonics
import com.openphonics.data.model.progress.LanguageProgress as LanguageProgressModel
import com.openphonics.Testing.INVALID_DATA_ID
import com.openphonics.Testing.UPDATED_CLASS_NAME
import com.openphonics.Testing.UPDATED_FLAG
import com.openphonics.Testing.UPDATED_FLAG_IMG
import com.openphonics.Testing.UPDATED_LANGUAGE_ID
import com.openphonics.Testing.UPDATED_LANGUAGE_NAME
import com.openphonics.Testing.UPDATED_LESSON_COUNT
import com.openphonics.Testing.UPDATED_NATIVE_ID
import com.openphonics.Testing.UPDATED_SENTENCE
import com.openphonics.Testing.UPDATED_TITLE
import com.openphonics.Testing.UPDATED_WORD
import com.openphonics.Testing.VALID_ADMIN_CLASS_CODE
import com.openphonics.Testing.VALID_CLASS_CODE
import com.openphonics.Testing.VALID_CLASS_NAME
import com.openphonics.Testing.VALID_FLAG
import com.openphonics.Testing.VALID_FLAG_IMG
import com.openphonics.Testing.VALID_LANGUAGE_NAME
import com.openphonics.Testing.VALID_LESSON_COUNT
import com.openphonics.Testing.VALID_NATIVE_ID
import com.openphonics.Testing.VALID_ORDER
import com.openphonics.Testing.VALID_PHONIC
import com.openphonics.Testing.VALID_SENTENCE
import com.openphonics.Testing.VALID_SOUND
import com.openphonics.Testing.VALID_TITLE
import com.openphonics.Testing.VALID_TRANSLATED_SOUND
import com.openphonics.Testing.VALID_TRANSLATED_WORD
import com.openphonics.Testing.VALID_WORD
import com.openphonics.model.request.*
import com.openphonics.model.response.*
import com.openphonics.tests.Auth.invalidRegisterAdminBlankName
import com.openphonics.tests.Auth.invalidRegisterAdminInvalidClassCode
import com.openphonics.tests.Auth.invalidRegisterAdminLongName
import com.openphonics.tests.Auth.invalidRegisterAdminNoSpaceName
import com.openphonics.tests.Auth.invalidRegisterAdminNumericName
import com.openphonics.tests.Auth.invalidRegisterAdminShortName
import com.openphonics.tests.Auth.invalidRegisterAdminWrongClassCode
import com.openphonics.tests.Auth.invalidRegisterUserBlankName
import com.openphonics.tests.Auth.invalidRegisterUserInvalidClassCode
import com.openphonics.tests.Auth.invalidRegisterUserLongName
import com.openphonics.tests.Auth.invalidRegisterUserNoSpaceName
import com.openphonics.tests.Auth.invalidRegisterUserNumericName
import com.openphonics.tests.Auth.invalidRegisterUserShortName
import com.openphonics.tests.Auth.invalidRegisterUserWrongClassCode
import com.openphonics.tests.Auth.login
import com.openphonics.tests.Auth.registerAdmin
import com.openphonics.tests.Auth.registerUser
import com.openphonics.tests.Auth.validLoginAdmin
import com.openphonics.tests.Auth.validLoginUser
import com.openphonics.tests.Auth.validRegisterAdmin
import com.openphonics.tests.Auth.validRegisterUser
import com.openphonics.tests.Classroom.createClass
import com.openphonics.tests.Classroom.deleteClass
import com.openphonics.tests.Classroom.getClassroom
import com.openphonics.tests.Classroom.invalidClassroomBlankClassCode
import com.openphonics.tests.Classroom.invalidClassroomLongName
import com.openphonics.tests.Classroom.invalidClassroomNumericName
import com.openphonics.tests.Classroom.updateClass
import com.openphonics.tests.Classroom.validClassroomRequest
import com.openphonics.tests.Flags.createFlag
import com.openphonics.tests.Flags.deleteFlagById
import com.openphonics.tests.Flags.getFlagById
import com.openphonics.tests.Flags.getFlags
import com.openphonics.tests.Flags.invalidFlagRequestLongFlag
import com.openphonics.tests.Flags.invalidFlagRequestNumericFlag
import com.openphonics.tests.Flags.updateFlagById
import com.openphonics.tests.Flags.validFlagRequest
import com.openphonics.tests.LanguageProgress
import com.openphonics.tests.LanguageProgress.createLanguageProgress
import com.openphonics.tests.LanguageProgress.deleteLanguageProgress
import com.openphonics.tests.LanguageProgress.getLanguageProgress
import com.openphonics.tests.LanguageProgress.getLanguageProgressById
import com.openphonics.tests.LanguageProgress.updateLanguageProgress
import com.openphonics.tests.LanguageProgress.updateStreakLanguageProgress
import com.openphonics.tests.Languages.createLanguage
import com.openphonics.tests.Languages.deleteLanguageById
import com.openphonics.tests.Languages.getLanguageById
import com.openphonics.tests.Languages.getLanguages
import com.openphonics.tests.Languages.invalidLanguageRequestLongLanguage
import com.openphonics.tests.Languages.invalidLanguageRequestLongName
import com.openphonics.tests.Languages.invalidLanguageRequestLongNative
import com.openphonics.tests.Languages.invalidLanguageRequestNumericLanguage
import com.openphonics.tests.Languages.invalidLanguageRequestNumericName
import com.openphonics.tests.Languages.invalidLanguageRequestNumericNative
import com.openphonics.tests.Languages.updateLanguageById
import com.openphonics.tests.Languages.validLanguageRequest
import com.openphonics.tests.SectionProgress.updateSectionProgress
import com.openphonics.tests.Sections.createSection
import com.openphonics.tests.Sections.deleteSectionById
import com.openphonics.tests.Sections.deleteSentencesToSection
import com.openphonics.tests.Sections.deleteWordsToSection
import com.openphonics.tests.Sections.getSectionById
import com.openphonics.tests.Sections.invalidSectionRequestInvalidLessonCount
import com.openphonics.tests.Sections.invalidSectionRequestInvalidLongTitle
import com.openphonics.tests.Sections.invalidSectionRequestInvalidNegativeOrder
import com.openphonics.tests.Sections.invalidSectionRequestInvalidNumericTitle
import com.openphonics.tests.Sections.postSentencesToSection
import com.openphonics.tests.Sections.postWordsToSection
import com.openphonics.tests.Sections.updateSectionById
import com.openphonics.tests.Sections.validSectionRequest
import com.openphonics.tests.Sentences.createSentence
import com.openphonics.tests.Sentences.deleteSentenceById
import com.openphonics.tests.Sentences.getSentenceById
import com.openphonics.tests.Sentences.sentenceRequest
import com.openphonics.tests.Sentences.updateSentenceById
import com.openphonics.tests.Units.createUnit
import com.openphonics.tests.Units.deleteUnitById
import com.openphonics.tests.Units.getUnitById
import com.openphonics.tests.Units.invalidUnitRequestInvalidLongTitle
import com.openphonics.tests.Units.invalidUnitRequestInvalidNegativeOrder
import com.openphonics.tests.Units.invalidUnitRequestInvalidNumericTitle
import com.openphonics.tests.Units.updateUnitById
import com.openphonics.tests.Units.validUnitRequest
import com.openphonics.tests.Words.createWord
import com.openphonics.tests.Words.deleteWordById
import com.openphonics.tests.Words.getWordById
import com.openphonics.tests.Words.invalidWordRequestInvalidPhonic
import com.openphonics.tests.Words.invalidWordRequestInvalidTranslatedWord
import com.openphonics.tests.Words.invalidWordRequestInvalidWord
import com.openphonics.tests.Words.updateWordById
import com.openphonics.tests.Words.validWordRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import org.junit.AfterClass
import org.junit.Assert.assertNotEquals
import org.junit.BeforeClass
import org.junit.Test
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import com.openphonics.data.model.data.Language as LanguageModel
import com.openphonics.data.model.data.Unit as UnitModel
import com.openphonics.data.model.data.Section as SectionModel


object Testing {

    const val INVALID_DATA_ID = -1

    const val VALID_NATIVE_ID = "en"
    const val UPDATED_NATIVE_ID = "en"
    const val INVALID_NUMERIC_NATIVE_ID = "e4"
    const val INVALID_LONG_NATIVE_ID = "enen"

    const val VALID_LANGUAGE_ID = "ta"
    const val UPDATED_LANGUAGE_ID = "ra"
    const val INVALID_NUMERIC_LANGUAGE_ID = "e4"
    const val INVALID_LONG_LANGUAGE_ID = "enen"

    const val VALID_LANGUAGE_NAME = "Tamil"
    const val UPDATED_LANGUAGE_NAME = "Engra"
    const val INVALID_NUMERIC_LANGUAGE_NAME = "T4s"
    const val INVALID_LONG_LANGUAGE_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

    const val VALID_TITLE = "Basic Words"
    const val UPDATED_TITLE = "Basic Numbers"
    const val INVALID_NUMERIC_TITLE = "B4 Wo4rs"
    const val INVALID_LONG_TITLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

    const val VALID_ORDER = 0
    const val INVALID_ORDER = -1

    const val VALID_LESSON_COUNT = 2
    const val UPDATED_LESSON_COUNT = 3
    const val INVALID_LESSON_COUNT = -1

    const val VALID_FLAG = "in"
    const val UPDATED_FLAG = "us"
    const val INVALID_LONG_FLAG = "ins"
    const val INVALID_NUMERIC_FLAG = "p4"

    const val VALID_FLAG_IMG = "https://raw.githubusercontent.com/catamphetamine/country-flag-icons/master/3x2/IN.svg"
    const val UPDATED_FLAG_IMG = "https://raw.githubusercontent.com/catamphetamine/country-flag-icons/master/3x2/US.svg"
    const val VALID_CLASS_CODE = "eg3a1r"
    const val VALID_ADMIN_CLASS_CODE = "e5fe1k"
    const val BLANK_CLASS_CODE = ""

    const val VALID_CLASS_NAME = "Bharathi Primary"
    const val UPDATED_CLASS_NAME = "Indian School"
    const val INVALID_LONG_CLASS_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
    const val INVALID_NUMERIC_CLASS_NAME = "A4"

    const val VALID_NAME = "Advait Vedant"
    const val INVALID_BLANK_NAME = ""
    const val INVALID_SHORT_NAME = "a b"
    const val INVALID_LONG_NAME = "AdvaitAdvait SurajUnnikrishnans"
    const val INVALID_NUMERIC_NAME = "Abd4 hello"
    const val INVALID_NO_SPACE_NAME = "Advit"

    const val VALID_PHONIC = "həˈləʊ"
    const val INVALID_PHONIC = "hte4t"
    const val VALID_WORD = "hello"
    const val UPDATED_WORD = "huh"
    const val INVALID_WORD= "he11o"
    const val VALID_TRANSLATED_WORD = "வணக்கம்"
    const val INVALID_TRANSLATED_WORD = "வணக4்கம்"
    const val VALID_SOUND = "https://api.dictionaryapi.dev/media/pronunciations/en/hello-au.mp3"
    const val VALID_TRANSLATED_SOUND = "https://api.dictionaryapi.dev/media/pronunciations/en/hello-au.mp3"

    val VALID_SENTENCE = listOf("apple", "banana", "orange", "cherry", "watermelon", "grape")
//    val VALID_SENTENCE_IDS = mutableListOf<Int>()
    val UPDATED_SENTENCE =  listOf("orange", "apple", "banana")
//    val UPDATED_SENTENCE_IDS = mutableListOf<Int>()
}

class ApplicationTest {
    companion object {
        @OptIn(KtorExperimentalAPI::class)
        val test: (suspend (HttpClient)->Unit) -> Unit = { testing->
            testApplication {
                environment {
                    config = MapApplicationConfig(
                        "key.admin" to "cf6b502cc6d73d9d0b915fad33e249b3c5960ab57225e8179861bf946eb53dee",
                        "key.secret" to "2gkF75yMsUDcZ0yQxIIak0be2TNUk4o0",
                        "database.host" to sqlContainer.host,
                        "database.port" to sqlContainer.firstMappedPort.toString(),
                        "database.name" to sqlContainer.databaseName,
                        "database.user" to sqlContainer.username,
                        "database.maxPoolSize" to "3",
                        "database.driver" to sqlContainer.driverClassName,
                        "database.password" to sqlContainer.password
                    )
                }
                application {
                    module()
                }
                val client = createClient {
                    install(ContentNegotiation) {
                        json(
                            json = Json {
                                prettyPrint = true
                            },
                            contentType = ContentType.Application.Json
                        )
                    }
                }
                testing(client)
            }
        }
        suspend inline fun <reified T> extractResponse(response: HttpResponse): T {
            return response.body<T>()
        }
        suspend fun bad(response: HttpResponse, message: String? = null) {
            assertEquals(HttpStatusCode.BadRequest, response.status , message ?: response.body())
        }
        suspend fun ok(response: HttpResponse, message: String? = null) {
            assertEquals(HttpStatusCode.OK, response.status, message ?: response.body())
        }
        suspend fun unauth(response: HttpResponse, message: String? = null){
            assertEquals(HttpStatusCode.Unauthorized, response.status, message ?: response.body())
        }
        suspend fun not(response: HttpResponse, message: String? = null){
            assertEquals(HttpStatusCode.NotFound, response.status, message ?: response.body())
        }
        private val sqlContainer = PostgreSQLContainer(DockerImageName.parse("postgres:13-alpine"))

        @JvmStatic
        @BeforeClass
        fun setUp() {
            sqlContainer.start()
        }

        @JvmStatic
        @AfterClass
        fun tearDown() {
            sqlContainer.stop()
        }
    }
    private suspend fun runRegisterAdminTests(client: HttpClient){
        bad(registerAdmin(invalidRegisterAdminBlankName, client))
        bad(registerAdmin(invalidRegisterAdminInvalidClassCode, client))
        bad(registerAdmin(invalidRegisterAdminLongName, client))
        bad(registerAdmin(invalidRegisterAdminShortName, client))
        bad(registerAdmin(invalidRegisterAdminNoSpaceName, client))
        bad(registerAdmin(invalidRegisterAdminNumericName, client))
        bad(registerAdmin(invalidRegisterAdminWrongClassCode, client))
        ok(registerAdmin(validRegisterAdmin, client))
        bad(registerAdmin(validRegisterAdmin, client))
    }
    private suspend fun runLoginAdminTests(client: HttpClient): String {
        unauth(login(LoginRequest("Random Name", VALID_ADMIN_CLASS_CODE), client))
        val response = login(validLoginAdmin, client)
        ok(response)
        val token = extractResponse<AuthResponse>(response).token
        assertNotNull(token)
        return token
    }
    private suspend fun runCreateClassroomTests(token: String, client: HttpClient): String {
        bad(createClass(token, invalidClassroomLongName, client))
        bad(createClass(token, invalidClassroomBlankClassCode, client))
        bad(createClass(token, invalidClassroomNumericName, client))
        val classResponse = createClass(token, validClassroomRequest, client)
        ok(classResponse)
        val classCode = extractResponse<StrIdResponse>(classResponse).id
        assertNotNull(classCode)
        bad(createClass(token, validClassroomRequest, client))
        return classCode
    }
    private suspend fun runCreateFlagTests(token: String, client: HttpClient): String {
        bad(createFlag(token, invalidFlagRequestLongFlag, client))
        bad(createFlag(token, invalidFlagRequestNumericFlag, client))
        val flag = createFlag(token, validFlagRequest, client)
        bad(createFlag(token, validFlagRequest, client))
        ok(flag)
        val flagId = extractResponse<StrIdResponse>(flag).id
        assertNotNull(flagId)
        return flagId
    }
    private suspend fun classroom(token: String, request: ClassroomRequest, client: HttpClient): String {
        val classroom = createClass(token, request, client)
        ok(classroom)
        val classCode = extractResponse<StrIdResponse>(classroom).id
        assertNotNull(classCode)
        return classCode
    }
    private suspend fun flag(token: String, request: FlagRequest, client: HttpClient): String {
        val flag = createFlag(token, request, client)
        ok(flag)
        val flagId = extractResponse<StrIdResponse>(flag).id
        assertNotNull(flagId)
        return flagId
    }
    private suspend fun language(token: String, request: LanguageRequest, client: HttpClient): Int {
        val language = createLanguage(token, request, client)
        ok(language)
        val languageId = extractResponse<IntIdResponse>(language).id
        assertNotNull(languageId)
        return languageId
    }
    private suspend fun unit(token: String, request: UnitRequest, client: HttpClient): Int {
        val unit = createUnit(token, request, client)
        ok(unit)
        val unitId = extractResponse<IntIdResponse>(unit).id
        assertNotNull(unitId)
        return unitId
    }
    private suspend fun section(token: String, request: SectionRequest, client: HttpClient): Int {
        val section = createSection(token, request, client)
        ok(section)
        val sectionId = extractResponse<IntIdResponse>(section).id
        assertNotNull(sectionId)
        return sectionId
    }
    private suspend fun word(token: String, request: WordRequest, client: HttpClient): Int {
        val word = createWord(token, request, client)
        ok(word)
        val wordId = extractResponse<IntIdResponse>(word).id
        assertNotNull(wordId)
        return wordId
    }
    private suspend fun sentence(token: String, request: SentenceRequest, client: HttpClient): Int {
        val sentence = createSentence(token, request, client)
        ok(sentence)
        val sentenceId = extractResponse<IntIdResponse>(sentence).id
        assertNotNull(sentenceId)
        return sentenceId
    }
    private suspend fun runCreateLanguageTestsWithAdminToken(token: String, flagId: String, client: HttpClient): Int{
        bad(createLanguage(token, invalidLanguageRequestNumericNative(flagId), client))
        bad(createLanguage(token, invalidLanguageRequestLongNative(flagId), client))
        bad(createLanguage(token, invalidLanguageRequestNumericLanguage(flagId), client))
        bad(createLanguage(token, invalidLanguageRequestLongLanguage(flagId), client))
        bad(createLanguage(token, invalidLanguageRequestNumericName(flagId), client))
        bad(createLanguage(token, invalidLanguageRequestLongName(flagId), client))
        val id = language(token, validLanguageRequest(flagId), client)
        bad(createLanguage(token, validLanguageRequest(flagId), client))
        return id
    }
    private suspend fun runCreateUnitTestsWithAdminToken(token: String, languageId: Int, client: HttpClient): Int {
        bad(createUnit(token, invalidUnitRequestInvalidLongTitle(languageId), client))
        bad(createUnit(token, invalidUnitRequestInvalidNegativeOrder(languageId), client))
        bad(createUnit(token, invalidUnitRequestInvalidNumericTitle(languageId), client))
        return unit(token,validUnitRequest(languageId), client)
    }
    private suspend fun runCreateSectionTestsWithAdminToken(token: String, unitId: Int, client: HttpClient): Int {
        bad(createSection(token, invalidSectionRequestInvalidLessonCount(unitId), client))
        bad(createSection(token, invalidSectionRequestInvalidLongTitle(unitId), client))
        bad(createSection(token, invalidSectionRequestInvalidNegativeOrder(unitId), client))
        bad(createSection(token, invalidSectionRequestInvalidNumericTitle(unitId), client))
        return section(token,validSectionRequest(unitId), client)
    }
    private suspend fun runCreateWordTestsWithAdminToken(token: String, languageId: Int, client: HttpClient): Int {
        bad(createWord(token, invalidWordRequestInvalidPhonic(languageId), client))
        bad(createWord(token, invalidWordRequestInvalidWord(languageId), client))
        bad(createWord(token, invalidWordRequestInvalidTranslatedWord(languageId), client))
        val id = word(token,validWordRequest(languageId), client)
        bad(createWord(token, validWordRequest(languageId), client))
        return id
    }
    private suspend fun runCreateSentenceTestsWithAdminToken(token: String, languageId: Int, client: HttpClient): Pair<Int, Map<String,Int>>{
        val words = VALID_SENTENCE.map {
            val response = createWord(token, WordRequest(VALID_PHONIC, VALID_SOUND, VALID_TRANSLATED_SOUND, VALID_TRANSLATED_WORD, it, languageId), client)
            ok(response)
            val word = extractResponse<IntIdResponse>(response).id
            assertNotNull(word)
            word
        }
        bad(createSentence(token, sentenceRequest(emptyList(), languageId), client))
        bad(createSentence(token, sentenceRequest(listOf(INVALID_DATA_ID), languageId), client))
        bad(createSentence(token, sentenceRequest(listOf(INVALID_DATA_ID), languageId), client))
        bad(createSentence(token, sentenceRequest(words, INVALID_DATA_ID), client))
        ok(createSentence(token, sentenceRequest(words, languageId), client))
        return sentence(token, sentenceRequest(words, languageId), client) to VALID_SENTENCE.mapIndexed {index, word-> word to words[index]}.toMap()
    }
    private suspend fun runAddWordToSectionTestsWithAdminToken(token: String, wordId: Int, sectionId: Int, client: HttpClient) {
        bad(postWordsToSection(token, INVALID_DATA_ID, WordSectionRequest(wordId), client))
        bad(postWordsToSection(token, sectionId, WordSectionRequest(INVALID_DATA_ID), client))
        ok(postWordsToSection(token, sectionId, WordSectionRequest(wordId), client))
        bad(postWordsToSection(token, sectionId, WordSectionRequest(wordId), client))
    }
    private suspend fun runAddSentenceToSectionTestsWithAdminToken(token: String, sentenceId: Int, sectionId: Int, client: HttpClient) {
        bad(postSentencesToSection(token, INVALID_DATA_ID, SentenceSectionRequest(sentenceId), client))
        bad(postSentencesToSection(token, sectionId, SentenceSectionRequest(INVALID_DATA_ID), client))
        ok(postSentencesToSection(token, sectionId, SentenceSectionRequest(sentenceId), client))
        bad(postSentencesToSection(token, sectionId, SentenceSectionRequest(sentenceId), client))
    }
    private suspend fun runCreateDataTestsWithUserToken(token: String, flagId: String, languageId: Int, unitId: Int, sectionId: Int, wordId: Int, sentenceId: Int, client: HttpClient){
        unauth(createFlag(token, validFlagRequest, client))
        unauth(createLanguage(token, validLanguageRequest(flagId), client))
        unauth(createUnit(token, validUnitRequest(languageId), client))
        unauth(createSection(token, validSectionRequest(unitId), client))
        unauth(createWord(token, validWordRequest(languageId), client))
        unauth(createSentence(token, sentenceRequest(listOf(wordId), languageId), client))
        unauth(postWordsToSection(token, sectionId, WordSectionRequest(wordId), client))
        unauth(postSentencesToSection(token, sectionId, SentenceSectionRequest(sentenceId), client))
    }
    private suspend fun runGetClassroomTests(token: String, classCode: String, client: HttpClient) {
        val classroomResponse = getClassroom(token, classCode, client)
        ok(classroomResponse)
        val classroomData = extractResponse<ClassroomResponse>(classroomResponse).classroom.firstOrNull()
        assertNotNull(classroomData)
        assertEquals(VALID_CLASS_CODE, classroomData.classCode)
        assertEquals(VALID_CLASS_NAME, classroomData.className)
    }
    private suspend fun runGetFlagTests(token: String, flagId: String, client: HttpClient){
        val flagResponse = getFlagById(token, flagId, client)
        ok(flagResponse)
        val flagData = extractResponse<FlagResponse>(flagResponse).flag.firstOrNull()
        assertNotNull(flagData)
        assertEquals(VALID_FLAG, flagData.id)
        assertEquals(VALID_FLAG_IMG, flagData.flag)
    }
    private suspend fun runGetLanguageTests(token: String, languageId: Int, client: HttpClient) {
        //get language data at depth one
        val languageResponseDepthOne = getLanguageById(token, languageId, client, null)
        ok(languageResponseDepthOne)
        val languageDataDepthOne = extractResponse<LanguageResponse>(languageResponseDepthOne).language.firstOrNull()
        assertNotNull(languageDataDepthOne)
        //validate that it contains depth one data
        assertEquals(false, languageDataDepthOne.hasData)
        assertEquals(emptyList(), languageDataDepthOne.units)

        //get language data at depth two
        val languageResponseDepthTwo = getLanguageById(token, languageId, client, DepthRequest(LanguageModel.LANGUAGE_WITH_UNITS))
        ok(languageResponseDepthTwo)
        val languageDataDepthTwo = extractResponse<LanguageResponse>(languageResponseDepthTwo).language.firstOrNull()
        assertNotNull(languageDataDepthTwo)
        //validate that it contains depth two data
        assertEquals(true, languageDataDepthTwo.hasData)
        assertNotEquals(emptyList<Unit>(), languageDataDepthTwo.units)
        //check unit for correct info
        val unit = languageDataDepthTwo.units.first()
        assertEquals(VALID_ORDER, unit.order)
        assertEquals(VALID_TITLE, unit.title)
        //check that unit has depth two data
        languageDataDepthTwo.units.forEach {
            assertEquals(false, it.hasData)
            assertEquals(emptyList(), it.sections)
        }
        //get language data at depth three
        val languageResponseDepthThree = getLanguageById(token, languageId, client, DepthRequest(LanguageModel.LANGUAGE_WITH_UNITS_WITH_SECTIONS))
        ok(languageResponseDepthThree)
        val languageDataDepthThree = extractResponse<LanguageResponse>(languageResponseDepthThree).language.firstOrNull()
        assertNotNull(languageDataDepthThree)
        //check language contains data
        assertEquals(true, languageDataDepthThree.hasData)
        assertNotEquals(emptyList<Unit>(), languageDataDepthThree.units)
        languageDataDepthThree.units.forEach { unit ->
            //Check unit has correct depth
            assertEquals(true, unit.hasData)
            assertNotEquals(emptyList<Section>(), unit.sections)
            //Check section has correct data
            val section = unit.sections.first()
            assertEquals(VALID_ORDER, section.order)
            assertEquals(VALID_TITLE, section.title)
            assertEquals(VALID_LESSON_COUNT, section.lessonCount)
            //Check section has correct depth
            unit.sections.forEach {
                assertEquals(false, it.hasData)
                assertEquals(emptyList(), it.sentences)
                assertEquals(emptyList(), it.words)
            }
        }
        //get language at depth four
        val languageResponseDepthFour = getLanguageById(token, languageId, client, DepthRequest(LanguageModel.LANGUAGE_WITH_UNITS_WITH_SECTION_WITH_LESSON_DATA))
        ok(languageResponseDepthFour)
        val languageDataDepthFour = extractResponse<LanguageResponse>(languageResponseDepthFour).language.firstOrNull()
        assertNotNull(languageDataDepthFour)
        assertEquals(true, languageDataDepthFour.hasData)
        assertNotEquals(emptyList<Unit>(), languageDataDepthFour.units)
        languageDataDepthFour.units.forEach { unit1 ->
            assertEquals(true, unit1.hasData)
            assertNotEquals(emptyList<Section>(), unit1.sections)
            unit1.sections.forEach {
                val sentence = it.sentences.first()
                val word = it.words.first()
                //check word has correct data
                assertEquals(VALID_PHONIC, word.phonic)
                assertEquals(VALID_SOUND, word.sound)
                assertEquals(VALID_TRANSLATED_SOUND, word.translatedSound)
                assertEquals(VALID_TRANSLATED_WORD, word.translatedWord)
                assertEquals(VALID_WORD, word.word)
                //check sentence has correct data
                sentence.sentence.forEachIndexed {index, word ->
                    assertEquals(VALID_SENTENCE[index], word.word)
                }
                assertEquals(true, it.hasData)
                assertNotEquals(emptyList<Word>(), it.words)
                assertNotEquals(emptyList<Sentence>(), it.sentences)
            }
        }
    }
    private suspend fun runGetUnitTests(token: String, unitId: Int, client: HttpClient){
        val unitResponseDepthOne = getUnitById(token, unitId, client, DepthRequest(UnitModel.UNIT))
        ok(unitResponseDepthOne)
        val unitDataDepthOne = extractResponse<UnitResponse>(unitResponseDepthOne).unit.firstOrNull()
        assertNotNull(unitDataDepthOne)
        assertEquals(false, unitDataDepthOne.hasData)
        assertEquals(emptyList(), unitDataDepthOne.sections)
    }
    private suspend fun runGetSectionTests(token: String, sectionId: Int, client: HttpClient){
        val sectionResponseDepthOne = getSectionById(token, sectionId, client, DepthRequest(SectionModel.SECTION))
        ok(sectionResponseDepthOne)
        val sectionDataDepthOne = extractResponse<SectionResponse>(sectionResponseDepthOne).section.firstOrNull()
        assertNotNull(sectionDataDepthOne)
        assertEquals(false, sectionDataDepthOne.hasData)
        assertEquals(emptyList(), sectionDataDepthOne.words)
        assertEquals(emptyList(), sectionDataDepthOne.sentences)
    }
    private suspend fun runGetWordTests(token: String, wordId: Int, client: HttpClient){
        val wordResponse = getWordById(token, wordId, client)
        ok(wordResponse)
        val word = extractResponse<WordResponse>(wordResponse).word.firstOrNull()
        assertNotNull(word)
        assertEquals(VALID_PHONIC, word.phonic)
        assertEquals(VALID_SOUND, word.sound)
        assertEquals(VALID_TRANSLATED_SOUND, word.translatedSound)
        assertEquals(VALID_TRANSLATED_WORD, word.translatedWord)
        assertEquals(VALID_WORD, word.word)
    }
    private suspend fun runGetSentenceTests(token: String, sentenceId: Int, wordId: Int, client: HttpClient){
        val sentenceResponse = getSentenceById(token, sentenceId, client)
        ok(sentenceResponse)
        val sentenceData = extractResponse<SentenceResponse>(sentenceResponse).sentence.firstOrNull()
        assertNotNull(sentenceData)
        sentenceData.sentence.forEachIndexed {index, word ->
            assertEquals(VALID_SENTENCE[index], word.word)
        }
    }
    private suspend fun runUpdatedClassroomTestsWithAdminToken(token: String, classCode: String, client: HttpClient){
        ok(updateClass(token, classCode, UpdateClassroomRequest(UPDATED_CLASS_NAME), client))
        val classResponseUpdated = getClassroom(token, classCode,client)
        ok(classResponseUpdated)
        val classDataUpdated = extractResponse<ClassroomResponse>(classResponseUpdated).classroom.firstOrNull()
        assertNotNull(classDataUpdated)
        assertEquals(UPDATED_CLASS_NAME, classDataUpdated.className)
    }
    private suspend fun runUpdatedFlagTestsWithAdminToken(token: String, flagId: String, client: HttpClient){
        ok(updateFlagById(token, flagId, UpdateFlagRequest(UPDATED_FLAG_IMG), client))
        val flagResponseUpdated = getFlagById(token, flagId, client)
        ok(flagResponseUpdated)
        val flagDataUpdated = extractResponse<FlagResponse>(flagResponseUpdated).flag.firstOrNull()
        assertNotNull(flagDataUpdated)
        assertEquals(UPDATED_FLAG_IMG, flagDataUpdated.flag)
    }
    private suspend fun runUpdatedLanguageTestsWithAdminToken(token: String, languageId: Int, client: HttpClient){
        val newFlagId = flag(token, FlagRequest(UPDATED_FLAG_IMG, UPDATED_FLAG), client)
        ok(updateLanguageById(token, languageId, LanguageRequest(UPDATED_NATIVE_ID, UPDATED_LANGUAGE_ID, UPDATED_LANGUAGE_NAME, newFlagId), client))
        val languageResponseUpdated = getLanguageById(token, languageId, client, DepthRequest(LanguageModel.LANGUAGE))
        ok(languageResponseUpdated)
        val languageDataUpdated = extractResponse<LanguageResponse>(languageResponseUpdated).language.firstOrNull()
        assertNotNull(languageDataUpdated)
        assertEquals(UPDATED_LANGUAGE_ID, languageDataUpdated.languageId)
        assertEquals(UPDATED_NATIVE_ID, languageDataUpdated.nativeId)
        assertEquals(UPDATED_LANGUAGE_NAME, languageDataUpdated.languageName)
        assertEquals(newFlagId, languageDataUpdated.flag)
    }
    private suspend fun runUpdatedUnitTestsWithAdminToken(token: String, unitId: Int, client: HttpClient){
        ok(updateUnitById(token, unitId, UpdateUnitRequest(UPDATED_TITLE, VALID_ORDER), client))
        val unitResponseUpdated = getUnitById(token, unitId, client, DepthRequest(UnitModel.UNIT))
        ok(unitResponseUpdated)
        val unitDataUpdated = extractResponse<UnitResponse>(unitResponseUpdated).unit.firstOrNull()
        assertNotNull(unitDataUpdated)
        assertEquals(UPDATED_TITLE, unitDataUpdated.title)
        assertEquals(VALID_ORDER, unitDataUpdated.order)
    }
    private suspend fun runUpdatedSectionTestsWithAdminToken(token: String, languageId: Int, sectionId: Int, client: HttpClient){
        val newUnitId = unit(token, UnitRequest(VALID_TITLE, VALID_ORDER, languageId), client)
        bad(updateSectionById(token, sectionId, SectionRequest(UPDATED_TITLE, VALID_ORDER, UPDATED_LESSON_COUNT, INVALID_DATA_ID), client))
        ok(updateSectionById(token, sectionId, SectionRequest(UPDATED_TITLE, VALID_ORDER, UPDATED_LESSON_COUNT, newUnitId), client))
        val sectionResponseUpdated = getSectionById(token, sectionId, client, DepthRequest(SectionModel.SECTION))
        ok(sectionResponseUpdated)
        val sectionDataUpdated = extractResponse<SectionResponse>(sectionResponseUpdated).section.firstOrNull()
        assertNotNull(sectionDataUpdated)
        assertEquals(UPDATED_TITLE, sectionDataUpdated.title)
        assertEquals(UPDATED_LESSON_COUNT, sectionDataUpdated.lessonCount)
        assertEquals(VALID_ORDER, sectionDataUpdated.order)
        assertEquals(newUnitId, sectionDataUpdated.unit)
    }
    private suspend fun runUpdatedWordTestsWithAdminToken(token: String, wordId: Int, client: HttpClient){
        ok(updateWordById(
            token,
            wordId,
            UpdateWordRequest(VALID_PHONIC, VALID_SOUND, VALID_TRANSLATED_SOUND, VALID_TRANSLATED_WORD, UPDATED_WORD),
            client
        ))
        val wordResponseUpdated = getWordById(token, wordId, client)
        ok(wordResponseUpdated)
        val wordDataUpdated = extractResponse<WordResponse>(wordResponseUpdated).word.firstOrNull()
        assertNotNull(wordDataUpdated)
        assertEquals(VALID_PHONIC, wordDataUpdated.phonic)
        assertEquals(VALID_SOUND, wordDataUpdated.sound)
        assertEquals(VALID_TRANSLATED_SOUND, wordDataUpdated.translatedSound)
        assertEquals(VALID_TRANSLATED_WORD, wordDataUpdated.translatedWord)
        assertEquals(UPDATED_WORD, wordDataUpdated.word)
    }
    private suspend fun runUpdatedSentenceTestsWithAdminToken(token: String, words: Map<String, Int>, sentenceId: Int, client: HttpClient){
        val wordIds = UPDATED_SENTENCE.map {words[it]!!}
        ok(updateSentenceById(token, sentenceId, UpdateSentenceRequest(wordIds), client))
        val sentenceResponseUpdated = getSentenceById(token, sentenceId, client)
        ok(sentenceResponseUpdated)
        val sentenceDataUpdated = extractResponse<SentenceResponse>(sentenceResponseUpdated).sentence.firstOrNull()
        assertNotNull(sentenceDataUpdated)
        sentenceDataUpdated.sentence.forEachIndexed { index, word ->
            assertEquals(UPDATED_SENTENCE[index],word.word )
        }

    }
    private suspend fun runUpdatedWordSectionTestsWithAdminToken(token: String, wordId: Int, words: Map<String, Int>, sectionId: Int, client: HttpClient) {
        ok(deleteWordsToSection(token, sectionId, WordSectionRequest(wordId), client))
        VALID_SENTENCE.forEach {
            ok(postWordsToSection(token, sectionId, WordSectionRequest(words[it]!!), client))
        }
        VALID_SENTENCE.forEach {
            bad(postWordsToSection(token, sectionId, WordSectionRequest(words[it]!!), client))
        }
        val sectionResponseWithWords = getSectionById(token, sectionId, client, DepthRequest(SectionModel.SECTIONS_WITH_LESSON_DATA))
        ok(sectionResponseWithWords)
        val sectionDataWithWords = extractResponse<SectionResponse>(sectionResponseWithWords).section.firstOrNull()
        assertNotNull(sectionDataWithWords)
        sectionDataWithWords.words.forEachIndexed { index, word ->
            assertEquals(VALID_SENTENCE[index], word.word)
        }
    }
    private suspend fun runUpdatedSentenceSectionTestsWithAdminToken(token: String, sentenceId: Int, sectionId: Int, client: HttpClient) {
        ok(deleteSentencesToSection(token, sectionId, SentenceSectionRequest(sentenceId), client))
        val sectionResponseWithWords = getSectionById(token, sectionId, client, DepthRequest(SectionModel.SECTIONS_WITH_LESSON_DATA))
        ok(sectionResponseWithWords)
        val sectionDataWithWords = extractResponse<SectionResponse>(sectionResponseWithWords).section.firstOrNull()
        assertNotNull(sectionDataWithWords)
        assertEquals(0, sectionDataWithWords.sentences.size)
        ok(postSentencesToSection(token, sectionId, SentenceSectionRequest(sentenceId), client))
        bad(postSentencesToSection(token, sectionId, SentenceSectionRequest(sentenceId), client))
    }
    private suspend fun runRegisterUserTests(languageId: Int, client: HttpClient) {
        bad(registerUser(invalidRegisterUserBlankName(languageId), client))
        bad(registerUser(invalidRegisterUserInvalidClassCode(languageId), client))
        bad(registerUser(invalidRegisterUserLongName(languageId), client))
        bad(registerUser(invalidRegisterUserShortName(languageId), client))
        bad(registerUser(invalidRegisterUserNoSpaceName(languageId), client))
        bad(registerUser(invalidRegisterUserNumericName(languageId), client))
        bad(registerUser(invalidRegisterUserWrongClassCode(languageId), client))
        ok(registerUser(validRegisterUser(languageId), client))
        bad(registerUser(validRegisterUser(languageId), client))
    }
    private suspend fun runLoginUserTests(client: HttpClient): String {
        unauth(login(LoginRequest("Random Name", VALID_CLASS_CODE), client))
        val response = login(validLoginUser, client)
        ok(response)
        val token = extractResponse<AuthResponse>(response).token
        assertNotNull(token)
        return token
    }
    private suspend fun runGetLanguageProgressTests(token: String, client: HttpClient, unitCount: Int, sectionCount: Int){
        val languageProgressResponse = getLanguageProgress(token, DepthRequest(LanguageProgressModel.LANGUAGE_WITH_UNITS_WITH_SECTION_WITH_LESSON_DATA), client)
        ok(languageProgressResponse)
        val languageProgressData = extractResponse<LanguageProgressResponse>(languageProgressResponse).progress.firstOrNull()
        assertNotNull(languageProgressData)
        assertEquals(unitCount, languageProgressData.units.size)
        var foundSectionCount = 0
        languageProgressData.units.forEach {
            foundSectionCount += it.sections.size
        }
        assertEquals(sectionCount, foundSectionCount)
    }
    private suspend fun runCreateLanguageProgressTests(adminToken: String, token: String,flagId: String,  client: HttpClient): String {
        val languageId = language(adminToken, LanguageRequest("en", "no", "Nana", flagId), client)
        val createResponse = createLanguageProgress(token, LanguageProgressRequest(languageId), client)
        ok(createResponse)
        val newProgressId = extractResponse<StrIdResponse>(createResponse).id
        assertNotNull(newProgressId)
        val langProgressResponse = getLanguageProgressById(token, newProgressId, null, client)
        ok(langProgressResponse)
        val langProgressData = extractResponse<LanguageProgressResponse>(langProgressResponse).progress.firstOrNull()
        assertNotNull(langProgressData)
        return langProgressData.progressId
    }
    private suspend fun runUpdateLanguageProgressTests(token: String, progressId: String, client: HttpClient) {
        ok(updateLanguageProgress(token, progressId, XPRequest(10), client))
        bad(updateStreakLanguageProgress(token, progressId, StreakRequest(true), client))
        val langProgressResponse = getLanguageProgressById(token, progressId, null, client)
        ok(langProgressResponse)
        val langProgressData = extractResponse<LanguageProgressResponse>(langProgressResponse).progress.firstOrNull()
        assertNotNull(langProgressData)
        assertEquals(10, langProgressData.xp)
        assertEquals(0, langProgressData.streak)
    }
    private suspend fun runUpdateSectionProgressTests(token: String, progressId: String, client: HttpClient){
        val languageProgressResponse = getLanguageProgressById(token, progressId, DepthRequest(LanguageProgressModel.LANGUAGE_WITH_UNITS_WITH_SECTIONS), client)
        ok(languageProgressResponse)
        val languageProgressData = extractResponse<LanguageProgressResponse>(languageProgressResponse).progress.firstOrNull()
        assertNotNull(languageProgressData)
        languageProgressData.units.forEach {
            it.sections.forEach {
                updateSectionProgress(token, it.progressId, SectionProgressRequest(1, true, emptyList()), client)
            }
        }
        val updatedLanguageProgressResponse = getLanguageProgressById(token, progressId, DepthRequest(LanguageProgressModel.LANGUAGE_WITH_UNITS_WITH_SECTIONS), client)
        ok(updatedLanguageProgressResponse)
        val updatedLanguageProgressData = extractResponse<LanguageProgressResponse>(updatedLanguageProgressResponse).progress.firstOrNull()
        assertNotNull(updatedLanguageProgressData)
        updatedLanguageProgressData.units.forEach {
            it.sections.forEach {
                assertEquals(1, it.currentLesson)
                assertEquals(true, it.isLegendary)
            }
        }
    }
    @Test
    fun test() = test() { client ->

        //register admin
        runRegisterAdminTests(client)

        //login admin
        val adminToken = runLoginAdminTests(client)

        //create classroom
        val classCode = runCreateClassroomTests(adminToken, client)

        //create flag
        val flagId = runCreateFlagTests(adminToken, client)

        //create language
        val languageId = runCreateLanguageTestsWithAdminToken(adminToken, flagId, client)

        //create unit
        val unitId = runCreateUnitTestsWithAdminToken(adminToken, languageId, client)

        //create section
        val sectionId = runCreateSectionTestsWithAdminToken(adminToken, unitId, client)

        //create word
        val wordId = runCreateWordTestsWithAdminToken(adminToken, languageId, client)

        //create sentence
        val (sentenceId, wordIds) = runCreateSentenceTestsWithAdminToken(adminToken, languageId, client)

        //add word to section
        runAddWordToSectionTestsWithAdminToken(adminToken, wordId, sectionId, client)

        //add sentence to section
        runAddSentenceToSectionTestsWithAdminToken(adminToken, sentenceId, sectionId, client)

        //register user
        runRegisterUserTests(languageId, client)

        //login user
        val userToken = runLoginUserTests(client)

        //check language progress
        runGetLanguageProgressTests(userToken, client, 1, 1)
        val progressId = runCreateLanguageProgressTests(adminToken, userToken, flagId, client)

        //try to create unauth data
        runCreateDataTestsWithUserToken(userToken, flagId, languageId, unitId, sectionId, wordId, sentenceId, client)

        //get classroom
        runGetClassroomTests(userToken, classCode, client)
        runGetClassroomTests(adminToken, classCode, client)

        //get flag
        runGetFlagTests(userToken, flagId, client)
        runGetFlagTests(adminToken, flagId, client)

        //get language
        runGetLanguageTests(userToken, languageId, client)
        runGetLanguageTests(adminToken, languageId, client)

        //get unit
        runGetUnitTests(userToken, unitId, client)
        runGetUnitTests(adminToken, unitId, client)

        //get section
        runGetSectionTests(userToken, sectionId, client)
        runGetSectionTests(adminToken, sectionId, client)

        //get word
        runGetWordTests(userToken, wordId, client)
        runGetWordTests(adminToken, wordId, client)

        //get sentence
        runGetSentenceTests(userToken, sentenceId, wordId, client)
        runGetSentenceTests(adminToken, sentenceId, wordId, client)

        //update language progress
        runUpdateLanguageProgressTests(userToken, progressId, client)
        runUpdateSectionProgressTests(userToken, progressId, client)
        ok(deleteLanguageProgress(userToken, progressId, client))
        //updated classroom
        runUpdatedClassroomTestsWithAdminToken(adminToken, classCode, client)

        //updated flag
        runUpdatedFlagTestsWithAdminToken(adminToken, flagId, client)

        //updated language
        runUpdatedLanguageTestsWithAdminToken(adminToken, languageId, client)

        //updated unit
        runUpdatedUnitTestsWithAdminToken(adminToken, unitId, client)

        //updated section
        runUpdatedSectionTestsWithAdminToken(adminToken, languageId, sectionId, client)

        //updated word
        runUpdatedWordTestsWithAdminToken(adminToken, wordId, client)

        //updated sentence
        runUpdatedSentenceTestsWithAdminToken(adminToken, wordIds, sentenceId, client)

        //updated word in sections
        runUpdatedWordSectionTestsWithAdminToken(adminToken, wordId, wordIds, sectionId, client)

        //Update Sentences in Section
        runUpdatedSentenceSectionTestsWithAdminToken(adminToken, sentenceId, sectionId, client)


        //test that language updates translate to progress updates
        val newUnitId = runCreateUnitTestsWithAdminToken(adminToken, languageId, client)
        val newSectionId = runCreateSectionTestsWithAdminToken(adminToken, unitId, client)
        runGetLanguageProgressTests(userToken, client, 3, 2)
        deleteUnitById(adminToken, newUnitId, client)
        deleteSectionById(adminToken, newSectionId, client)
        runGetLanguageProgressTests(userToken, client, 2, 1)


        ok(deleteClass(adminToken, classCode, client))
        bad(deleteClass(adminToken, classCode, client))
        bad(getClassroom(adminToken, classCode, client))

        ok(deleteWordById(adminToken, wordIds[VALID_SENTENCE[0]]!!, client))
        not(deleteWordById(adminToken, wordIds[VALID_SENTENCE[0]]!!, client))
        not(getWordById(adminToken, wordIds[VALID_SENTENCE[0]]!!, client))
        not(getSentenceById(adminToken, sentenceId, client))

        not(deleteSentenceById(adminToken, sentenceId, client))


        ok(deleteLanguageById(adminToken, languageId, client))
        not(getLanguageById(adminToken, languageId, client, null))
        not(getUnitById(adminToken, unitId, client, null))
        not(getSectionById(adminToken, sectionId, client, null))
        ok(deleteFlagById(adminToken, flagId, client))
    }
}
