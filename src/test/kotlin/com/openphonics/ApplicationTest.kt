package com.openphonics
import com.openphonics.Testing.VALID_LANGUAGE_ID
import com.openphonics.Testing.VALID_LANGUAGE_NAME
import com.openphonics.Testing.VALID_NATIVE_ID
import com.openphonics.Testing.VALID_ORDER
import com.openphonics.Testing.VALID_PHONIC
import com.openphonics.Testing.VALID_SOUND
import com.openphonics.Testing.VALID_TRANSLATED_SOUND
import com.openphonics.Testing.VALID_TRANSLATED_WORD
import com.openphonics.Testing.VALID_WORD
import com.openphonics.Testing.VALID_WORDS
import com.openphonics.model.request.*
import com.openphonics.model.response.*
import com.openphonics.tests.Auth.invalidRegisterAdminBlankName
import com.openphonics.tests.Auth.invalidRegisterAdminInvalidClassCode
import com.openphonics.tests.Auth.invalidRegisterAdminLongName
import com.openphonics.tests.Auth.invalidRegisterAdminNoSpaceName
import com.openphonics.tests.Auth.invalidRegisterAdminNumericName
import com.openphonics.tests.Auth.invalidRegisterAdminShortName
import com.openphonics.tests.Auth.invalidRegisterAdminWrongClassCode
import com.openphonics.tests.Auth.login
import com.openphonics.tests.Auth.registerAdmin
import com.openphonics.tests.Auth.validLoginAdmin
import com.openphonics.tests.Auth.validRegisterAdmin
import com.openphonics.tests.Classroom.createClass
import com.openphonics.tests.Classroom.invalidClassroomBlankClassCode
import com.openphonics.tests.Classroom.invalidClassroomLongName
import com.openphonics.tests.Classroom.invalidClassroomNumericName
import com.openphonics.tests.Classroom.validClassroom
import com.openphonics.tests.Flags.createFlag
import com.openphonics.tests.Flags.deleteFlagById
import com.openphonics.tests.Flags.getFlagById
import com.openphonics.tests.Flags.getFlags
import com.openphonics.tests.Flags.invalidFlagRequestLongFlag
import com.openphonics.tests.Flags.invalidFlagRequestNumericFlag
import com.openphonics.tests.Flags.updateFlagById
import com.openphonics.tests.Flags.validFlagRequest
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
import com.openphonics.tests.Words.wordRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.statement.*
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
import com.openphonics.data.model.data.Language as LanguageModel
import com.openphonics.data.model.data.Unit as UnitModel
import com.openphonics.data.model.data.Section as SectionModel


object Testing {
    const val VALID_NATIVE_ID = "en"
    const val INVALID_NUMERIC_NATIVE_ID = "e4"
    const val INVALID_LONG_NATIVE_ID = "enen"

    const val VALID_LANGUAGE_ID = "ta"
    const val INVALID_NUMERIC_LANGUAGE_ID = "e4"
    const val INVALID_LONG_LANGUAGE_ID = "enen"

    const val VALID_LANGUAGE_NAME = "Tamil"
    const val INVALID_NUMERIC_LANGUAGE_NAME = "T4s"
    const val INVALID_LONG_LANGUAGE_NAME = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

    const val VALID_TITLE = "Basic Words"
    const val INVALID_NUMERIC_TITLE = "B4 Wo4rs"
    const val INVALID_LONG_TITLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"

    const val VALID_ORDER = 0
    const val INVALID_ORDER = -1

    const val VALID_LESSON_COUNT = 2
    const val INVALID_LESSON_COUNT = -1

    const val VALID_FLAG = "in"
    const val INVALID_LONG_FLAG = "ins"
    const val INVALID_NUMERIC_FLAG = "p4"

    const val VALID_FLAG_IMG = "https://raw.githubusercontent.com/catamphetamine/country-flag-icons/master/3x2/IN.svg"

    const val VALID_CLASS_CODE = "eg3a1r"
    const val VALID_ADMIN_CLASS_CODE = "e5fe1k"
    const val BLANK_CLASS_CODE = ""

    const val VALID_CLASS_NAME = "Bharathi Primary"
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
    val VALID_WORDS =  listOf("apple", "banana", "orange", "cherry", "watermelon", "grape")
    const val INVALID_WORD= "he11o"
    const val VALID_TRANSLATED_WORD = "வணக்கம்"
    const val INVALID_TRANSLATED_WORD = "வணக4்கம்"
    const val VALID_SOUND = "https://api.dictionaryapi.dev/media/pronunciations/en/hello-au.mp3"
    const val VALID_TRANSLATED_SOUND = "https://api.dictionaryapi.dev/media/pronunciations/en/hello-au.mp3"
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
        suspend fun checkBadRequestStatus(response: HttpResponse, message: String? = null) {
            assertEquals(HttpStatusCode.BadRequest, response.status , message ?: response.body())
        }

        suspend fun checkOKStatus(response: HttpResponse, message: String? = null) {
            assertEquals(HttpStatusCode.OK, response.status, message ?: response.body())
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
    @Test
    fun test() = test() { client ->

        //Register Admin
        checkBadRequestStatus(registerAdmin(invalidRegisterAdminBlankName, client))
        checkBadRequestStatus(registerAdmin(invalidRegisterAdminInvalidClassCode, client))
        checkBadRequestStatus(registerAdmin(invalidRegisterAdminLongName, client))
        checkBadRequestStatus(registerAdmin(invalidRegisterAdminShortName, client))
        checkBadRequestStatus(registerAdmin(invalidRegisterAdminNoSpaceName, client))
        checkBadRequestStatus(registerAdmin(invalidRegisterAdminNumericName, client))
        checkBadRequestStatus(registerAdmin(invalidRegisterAdminWrongClassCode, client))
        checkOKStatus(registerAdmin(validRegisterAdmin, client))
        checkBadRequestStatus(registerAdmin(validRegisterAdmin, client))

        //Login Admin
        val response = login(validLoginAdmin, client)
        checkOKStatus(response)
        val token = extractResponse<AuthResponse>(response).token
        assertNotNull(token)

        //Create Classroom
        checkBadRequestStatus(createClass(token, invalidClassroomLongName, client))
        checkBadRequestStatus(createClass(token, invalidClassroomBlankClassCode, client))
        checkBadRequestStatus(createClass(token, invalidClassroomNumericName, client))
        checkOKStatus(createClass(token, validClassroom, client))
        checkBadRequestStatus(createClass(token, validClassroom, client))

        //Create Flag
        checkBadRequestStatus(createFlag(token, invalidFlagRequestLongFlag, client))
        checkBadRequestStatus(createFlag(token, invalidFlagRequestNumericFlag, client))
        val flag = createFlag(token, validFlagRequest, client)
        checkOKStatus(flag)
        val flagId = extractResponse<StrIdResponse>(flag).id
        assertNotNull(flagId)

        //Create Language
        checkBadRequestStatus(createLanguage(token, invalidLanguageRequestNumericNative(flagId), client))
        checkBadRequestStatus(createLanguage(token, invalidLanguageRequestLongNative(flagId), client))
        checkBadRequestStatus(createLanguage(token, invalidLanguageRequestNumericLanguage(flagId), client))
        checkBadRequestStatus(createLanguage(token, invalidLanguageRequestLongLanguage(flagId), client))
        checkBadRequestStatus(createLanguage(token, invalidLanguageRequestNumericName(flagId), client))
        checkBadRequestStatus(createLanguage(token, invalidLanguageRequestLongName(flagId), client))
        val language = createLanguage(token, validLanguageRequest(flagId), client)
        checkOKStatus(language)
        val languageId = extractResponse<IntIdResponse>(language).id
        assertNotNull(languageId)
        checkBadRequestStatus(createLanguage(token, validLanguageRequest(flagId), client))

        //Create Unit
        checkBadRequestStatus(createUnit(token, invalidUnitRequestInvalidLongTitle(languageId), client))
        checkBadRequestStatus(createUnit(token, invalidUnitRequestInvalidNegativeOrder(languageId), client))
        checkBadRequestStatus(createUnit(token, invalidUnitRequestInvalidNumericTitle(languageId), client))
        val unit = createUnit(token, validUnitRequest(languageId), client)
        checkOKStatus(unit)
        val unitId = extractResponse<IntIdResponse>(unit).id
        assertNotNull(unitId)

        //Create Section
        checkBadRequestStatus(createSection(token, invalidSectionRequestInvalidLessonCount(unitId), client))
        checkBadRequestStatus(createSection(token, invalidSectionRequestInvalidLongTitle(unitId), client))
        checkBadRequestStatus(createSection(token, invalidSectionRequestInvalidNegativeOrder(unitId), client))
        checkBadRequestStatus(createSection(token, invalidSectionRequestInvalidNumericTitle(unitId), client))
        val section = createSection(token, validSectionRequest(unitId), client)
        checkOKStatus(section)
        val sectionId = extractResponse<IntIdResponse>(section).id
        assertNotNull(sectionId)

        //Create Word
        checkBadRequestStatus(createWord(token, invalidWordRequestInvalidPhonic(languageId), client))
        checkBadRequestStatus(createWord(token, invalidWordRequestInvalidWord(languageId), client))
        checkBadRequestStatus(createWord(token, invalidWordRequestInvalidTranslatedWord(languageId), client))
        val word = createWord(token, validWordRequest(languageId), client)
        checkOKStatus(word)
        val wordId = extractResponse<IntIdResponse>(word).id
        assertNotNull(wordId)

        //Creating words for sentence
        val words: List<Int> = VALID_WORDS.map { text ->
            val res = createWord(
                token, WordRequest(
                    VALID_PHONIC,
                    VALID_SOUND,
                    VALID_TRANSLATED_SOUND,
                    VALID_TRANSLATED_WORD,
                    text,
                    languageId
                ), client
            )
            checkOKStatus(res)
            val resId = extractResponse<IntIdResponse>(res).id
            assertNotNull(resId)
            resId
        }

        //Create Sentence
        val sentence = createSentence(token, sentenceRequest(words, languageId), client)
        checkOKStatus(sentence)
        val sentenceId = extractResponse<IntIdResponse>(sentence).id
        assertNotNull(sentenceId)


        //Add Words to Section
        words.forEach { word5 ->
            checkOKStatus(postWordsToSection(token, sectionId, WordSectionRequest(word5), client))
        }
        words.forEach { word6 ->
            checkBadRequestStatus(postWordsToSection(token, sectionId, WordSectionRequest(word6), client))
        }


        //Add Sentence to Section
        checkOKStatus(postSentencesToSection(token, sectionId, SentenceSectionRequest(sentenceId), client))
        checkBadRequestStatus(postSentencesToSection(token, sectionId, SentenceSectionRequest(sentenceId), client))


        //Get Language
        val languageResponseDepthOne = getLanguageById(token, languageId, client, null)
        checkOKStatus(languageResponseDepthOne)
        val languageDataDepthOne = extractResponse<LanguageResponse>(languageResponseDepthOne).language.firstOrNull()
        assertNotNull(languageDataDepthOne)
        assertEquals(false, languageDataDepthOne.hasData)
        assertEquals(emptyList(), languageDataDepthOne.units)

        val languageResponseDepthTwo =
            getLanguageById(token, languageId, client, DepthRequest(LanguageModel.LANGUAGE_WITH_UNITS))
        checkOKStatus(languageResponseDepthTwo)
        val languageDataDepthTwo = extractResponse<LanguageResponse>(languageResponseDepthTwo).language.firstOrNull()
        assertNotNull(languageDataDepthTwo)
        assertEquals(true, languageDataDepthTwo.hasData)
        assertNotEquals(emptyList<Unit>(), languageDataDepthTwo.units)
        languageDataDepthTwo.units.forEach {
            assertEquals(false, it.hasData)
            assertEquals(emptyList(), it.sections)
        }

        val languageResponseDepthThree =
            getLanguageById(token, languageId, client, DepthRequest(LanguageModel.LANGUAGE_WITH_UNITS_WITH_SECTIONS))
        checkOKStatus(languageResponseDepthThree)
        val languageDataDepthThree =
            extractResponse<LanguageResponse>(languageResponseDepthThree).language.firstOrNull()
        assertNotNull(languageDataDepthThree)
        assertEquals(true, languageDataDepthThree.hasData)
        assertNotEquals(emptyList<Unit>(), languageDataDepthThree.units)
        languageDataDepthThree.units.forEach { unit1 ->
            assertEquals(true, unit1.hasData)
            assertNotEquals(emptyList<Section>(), unit1.sections)
            unit1.sections.forEach {
                assertEquals(false, it.hasData)
                assertEquals(emptyList(), it.sentences)
                assertEquals(emptyList(), it.words)
            }
        }

        val languageResponseDepthFour = getLanguageById(
            token,
            languageId,
            client,
            DepthRequest(LanguageModel.LANGUAGE_WITH_UNITS_WITH_SECTION_WITH_LESSON_DATA)
        )
        checkOKStatus(languageResponseDepthFour)
        val languageDataDepthFour = extractResponse<LanguageResponse>(languageResponseDepthFour).language.firstOrNull()
        assertNotNull(languageDataDepthFour)
        assertEquals(true, languageDataDepthFour.hasData)
        assertNotEquals(emptyList<Unit>(), languageDataDepthFour.units)
        languageDataDepthFour.units.forEach { unit1 ->
            assertEquals(true, unit1.hasData)
            assertNotEquals(emptyList<Section>(), unit1.sections)
            unit1.sections.forEach {
                assertEquals(true, it.hasData)
                assertNotEquals(emptyList<Section>(), it.words)
                assertNotEquals(emptyList<Section>(), it.sentences)
            }
        }

        //Get Unit
        val unitResponseDepthOne = getUnitById(token, unitId, client, DepthRequest(UnitModel.UNIT))
        checkOKStatus(unitResponseDepthOne)
        val unitDataDepthOne = extractResponse<UnitResponse>(unitResponseDepthOne).unit.firstOrNull()
        assertNotNull(unitDataDepthOne)
        assertEquals(false, unitDataDepthOne.hasData)
        assertEquals(emptyList(), unitDataDepthOne.sections)

        //Get Section
        val sectionResponseDepthOne = getSectionById(token, sectionId, client, DepthRequest(SectionModel.SECTION))
        checkOKStatus(sectionResponseDepthOne)
        val sectionDataDepthOne = extractResponse<SectionResponse>(sectionResponseDepthOne).section.firstOrNull()
        assertNotNull(sectionDataDepthOne)
        assertEquals(false, sectionDataDepthOne.hasData)
        assertEquals(emptyList(), sectionDataDepthOne.words)
        assertEquals(emptyList(), sectionDataDepthOne.sentences)

        //Get Word
        val wordResponse = getWordById(token, wordId, client)
        checkOKStatus(wordResponse)
        val wordData = extractResponse<WordResponse>(wordResponse).word.firstOrNull()
        assertNotNull(wordData)
        assertEquals(VALID_PHONIC, wordData.phonic)
        assertEquals(VALID_SOUND, wordData.sound)
        assertEquals(VALID_TRANSLATED_SOUND, wordData.translatedSound)
        assertEquals(VALID_TRANSLATED_WORD, wordData.translatedWord)
        assertEquals(VALID_WORD, wordData.word)
        assertEquals(languageId, wordData.language)

        //Get Sentence
        val sentenceResponse = getSentenceById(token, sentenceId, client)
        checkOKStatus(sentenceResponse)
        val sentenceData = extractResponse<SentenceResponse>(sentenceResponse).sentence.firstOrNull()
        assertNotNull(sentenceData)
        sentenceData.sentence.forEachIndexed { index, word1 ->
            assertEquals(word1.word, VALID_WORDS[index])
        }

        //Updated Flag
        val newFlagImg = "https://raw.githubusercontent.com/catamphetamine/country-flag-icons/master/3x2/US.svg"
        checkOKStatus(updateFlagById(token, FlagRequest(newFlagImg, flagId), client))
        val flagResponseUpdated = getFlagById(token, flagId, client)
        checkOKStatus(flagResponseUpdated)
        val flagDataUpdated = extractResponse<FlagResponse>(flagResponseUpdated).flag.firstOrNull()
        assertNotNull(flagDataUpdated)
        assertEquals(newFlagImg, flagDataUpdated.flag)

        //New Flag
        val newFlag = createFlag(token, FlagRequest(newFlagImg, "us"), client)
        checkOKStatus(newFlag)
        val newFlagId = extractResponse<StrIdResponse>(newFlag).id
        assertNotNull(newFlagId)

        //Update Language
        val newLanguageLanguageId = "ra"
        checkOKStatus(updateLanguageById(
            token,
            languageId,
            LanguageRequest(VALID_NATIVE_ID, newLanguageLanguageId, VALID_LANGUAGE_NAME, newFlagId),
            client
        ))
        val languageResponseUpdated = getLanguageById(token, languageId, client, DepthRequest(LanguageModel.LANGUAGE))
        checkOKStatus(languageResponseUpdated)
        val languageDataUpdated = extractResponse<LanguageResponse>(languageResponseUpdated).language.firstOrNull()
        assertNotNull(languageDataUpdated)
        assertEquals(newLanguageLanguageId, languageDataUpdated.languageId)
        assertEquals(VALID_NATIVE_ID, languageDataUpdated.nativeId)
        assertEquals(newFlagId, languageDataUpdated.flag)

        //Create New Language
        val newLanguage = createLanguage(token, validLanguageRequest(flagId), client)
        checkOKStatus(newLanguage)
        val newLanguageId = extractResponse<IntIdResponse>(newLanguage).id
        assertNotNull(newLanguageId)

        //Update Unit
        val newTitle = "Random Title"
        val newLessonCount = 5
        checkOKStatus(updateUnitById(token, unitId, UpdateUnitRequest(newTitle, VALID_ORDER), client))
        val unitResponseUpdated = getUnitById(token, unitId, client, DepthRequest(UnitModel.UNIT))
        checkOKStatus(unitResponseUpdated)
        val unitDataUpdated = extractResponse<UnitResponse>(unitResponseUpdated).unit.firstOrNull()
        assertNotNull(unitDataUpdated)
        assertEquals(newTitle, unitDataUpdated.title)
        assertEquals(VALID_ORDER, unitDataUpdated.order)

        //Create New Unit
        val newUnit = createUnit(token, validUnitRequest(newLanguageId), client)
        checkOKStatus(newUnit)
        val newUnitId = extractResponse<IntIdResponse>(newUnit).id
        assertNotNull(newUnitId)

        val newUnitOK = createUnit(token, validUnitRequest(languageId), client)
        checkOKStatus(newUnitOK)
        val newUnitIdOK = extractResponse<IntIdResponse>(newUnitOK).id
        assertNotNull(newUnitIdOK)

        //Update Section
        checkBadRequestStatus(updateSectionById(token, sectionId, SectionRequest(newTitle, VALID_ORDER, newLessonCount, newUnitId), client))
        checkOKStatus(updateSectionById(token, sectionId, SectionRequest(newTitle, VALID_ORDER, newLessonCount, newUnitIdOK), client))
        val sectionResponseUpdated = getSectionById(token, sectionId, client, DepthRequest(SectionModel.SECTION))
        checkOKStatus(sectionResponseUpdated)
        val sectionDataUpdated = extractResponse<SectionResponse>(sectionResponseUpdated).section.firstOrNull()
        assertNotNull(sectionDataUpdated)
        assertEquals(newTitle, sectionDataUpdated.title)
        assertEquals(newLessonCount, sectionDataUpdated.lessonCount)
        assertEquals(VALID_ORDER, sectionDataUpdated.order)
        assertEquals(newUnitIdOK, sectionDataUpdated.unit)

        //Update Word
        val newWord = "random"
        checkOKStatus(updateWordById(
            token,
            wordId,
            UpdateWordRequest(VALID_PHONIC, VALID_SOUND, VALID_TRANSLATED_SOUND, VALID_TRANSLATED_WORD, newWord),
            client
        ))
        val wordResponseUpdated = getWordById(token, wordId, client)
        checkOKStatus(wordResponseUpdated)
        val wordDataUpdated = extractResponse<WordResponse>(wordResponseUpdated).word.firstOrNull()
        assertNotNull(wordDataUpdated)
        assertEquals(VALID_PHONIC, wordDataUpdated.phonic)
        assertEquals(VALID_SOUND, wordDataUpdated.sound)
        assertEquals(VALID_TRANSLATED_SOUND, wordDataUpdated.translatedSound)
        assertEquals(VALID_TRANSLATED_WORD, wordDataUpdated.translatedWord)
        assertEquals(newWord, wordDataUpdated.word)

        //Updated Sentence
        val newWordIdList = words.shuffled() + words.shuffled()
        val newWordList = newWordIdList.map { id ->
            val res = getWordById(token, id, client)
            checkOKStatus(res)
            val resWord = extractResponse<WordResponse>(res).word.firstOrNull()
            assertNotNull(resWord)
            resWord.word
        }
        checkOKStatus(updateSentenceById(token, sentenceId, UpdateSentenceRequest(newWordIdList), client))
        val sentenceResponseUpdated = getSentenceById(token, sentenceId, client)
        checkOKStatus(sentenceResponseUpdated)
        val sentenceDataUpdated = extractResponse<SentenceResponse>(sentenceResponseUpdated).sentence.firstOrNull()
        assertNotNull(sentenceDataUpdated)
        sentenceDataUpdated.sentence.forEachIndexed { index, word1 ->
            assertEquals(word1.word, newWordList[index])
        }

        //Update Words in Section
        val newSectionWordIds = words.subList(0, 2)
        val newSectionWords = newSectionWordIds.map { id ->
            val res = getWordById(token, id, client)
            checkOKStatus(res)
            val resWord = extractResponse<WordResponse>(res).word.firstOrNull()
            assertNotNull(resWord)
            resWord.word
        }
        words.forEach { word7 ->
            checkOKStatus(deleteWordsToSection(token, sectionId, WordSectionRequest(word7), client))
        }
        newSectionWordIds.forEach {word8 ->
            checkOKStatus(postWordsToSection(token, sectionId, WordSectionRequest(word8), client))
        }
        val sectionResponseWithWords =
            getSectionById(token, sectionId, client, DepthRequest(SectionModel.SECTIONS_WITH_LESSON_DATA))
        checkOKStatus(sentenceResponseUpdated)
        val sectionDataWithWords = extractResponse<SectionResponse>(sectionResponseWithWords).section.firstOrNull()
        assertNotNull(sectionDataWithWords)
        sectionDataWithWords.words.forEachIndexed { index, word1 ->
            assertEquals(word1.word, newSectionWords[index])
        }

        //Update Sentences in Section
        val newSectionSentenceData = (1..4).map { words.shuffled() }
        val newSectionSentences = newSectionSentenceData.map { sentence3 ->
            val res = createSentence(token, SentenceRequest(languageId, sentence3), client)
            checkOKStatus(res)
            val resSentence = extractResponse<IntIdResponse>(res).id
            assertNotNull(resSentence)
            resSentence
        }
        checkOKStatus(deleteSentencesToSection(token, sectionId, SentenceSectionRequest(sentenceId), client))
        newSectionSentences.forEach { sentence4 ->
            checkOKStatus(postSentencesToSection(token, sectionId, SentenceSectionRequest(sentence4), client))
        }
        val sectionResponseWithSentences =
            getSectionById(token, sectionId, client, DepthRequest(SectionModel.SECTIONS_WITH_LESSON_DATA))
        checkOKStatus(sentenceResponseUpdated)
        val sectionDataWithSentences =
            extractResponse<SectionResponse>(sectionResponseWithSentences).section.firstOrNull()
        assertNotNull(sectionDataWithSentences)
        assertEquals(4, sectionDataWithSentences.sentences.size)
        sectionDataWithSentences.sentences.forEachIndexed { index, sentence1 ->
            sentence1.sentence.forEachIndexed { wordIndex, word ->
                assertEquals(word.id, newSectionSentenceData[index][wordIndex])
            }
        }

        val flags = getFlags(token, client)
        checkOKStatus(flags)
        val flagsData = extractResponse<FlagResponse>(flags).flag
        assertEquals(2, flagsData.size)

        val languages = getLanguages(token, client, DepthRequest(LanguageModel.LANGUAGE))
        checkOKStatus(languages)
        val languagesData = extractResponse<LanguageResponse>(languages).language
        assertEquals(2, languagesData.size)

        //Should clear all language data
        checkOKStatus(deleteSentenceById(token, sentenceId, client))
        checkOKStatus(deleteWordById(token, wordId, client))
        checkOKStatus(deleteSectionById(token, sectionId, client))
        checkOKStatus(deleteUnitById(token, unitId, client))
        checkOKStatus(deleteLanguageById(token, languageId, client))
        checkOKStatus(deleteLanguageById(token, newLanguageId, client))
        words.forEach {word2->
            deleteWordById(token, word2, client)
        }
        newSectionSentences.forEach { sentence1 ->
            deleteSentenceById(token, sentence1, client)
        }
        checkOKStatus(deleteFlagById(token, newFlagId, client))
        checkOKStatus(deleteFlagById(token, flagId, client))
    }
}
