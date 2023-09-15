package com.openphonics.controller

import com.openphonics.exception.BadRequestException
import com.openphonics.exception.NotFoundException
import com.openphonics.exception.UnauthorizedActivityException
import com.openphonics.model.request.*
import com.openphonics.model.response.*
import com.openphonics.model.response.Unit
import com.openphonics.data.dao.DataDaoImpl
import com.openphonics.data.entity.data.*
import com.openphonics.data.model.user.User
import com.openphonics.utils.containsOnlyLetters
import javax.inject.Inject
import javax.inject.Singleton
import com.openphonics.data.model.data.Section as SectionModel
import com.openphonics.data.model.data.Unit as UnitModel

@Singleton
class DataController @Inject constructor(
    private val dataDao: DataDaoImpl
) {
    fun getAllLanguages(native: String, depth: DepthRequest): LanguageResponse{
        return try {
            validateDepthOrThrowException(depth.depth)
            checkNativeExistsOrThrowException(native)
            val languages = dataDao.getLanguages(native, depth.depth)
            LanguageResponse.success(languages.map { Language.create(it)})
        } catch (bre: BadRequestException) {
            LanguageResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            LanguageResponse.unauthorized(uae.message)
        }
    }
    fun getLanguage(languageId: Int, depth: DepthRequest): LanguageResponse {
        return try {
            validateDepthOrThrowException(depth.depth)
            checkDataExistsOrThrowException(languageId, EntityLanguage)
            val language = dataDao.getLanguageById(languageId, depth.depth)!!
            LanguageResponse.success(Language.create(language))
        } catch (bre: BadRequestException) {
            LanguageResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            LanguageResponse.unauthorized(uae.message)
        } catch (nfe: NotFoundException) {
            LanguageResponse.notFound(nfe.message)
        }
    }
    fun addLanguage(user: User, language: LanguageRequest): IntIdResponse {
        return try {
            val nativeId = language.nativeId
            val languageId  = language.languageId
            val languageName = language.languageName
            val flag = language.flag
            validateLanguageRequestOrThrowException(nativeId, languageId, languageName, flag)
            validateAdmin(user)
            val responseId = dataDao.addLanguage(nativeId, languageId, languageName, flag)
            IntIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun updateLanguage(user: User, id: Int, language: LanguageRequest): IntIdResponse {
        return try {
            val nativeId = language.nativeId
            val languageId  = language.languageId
            val languageName = language.languageName
            val flag = language.flag
            if (!dataDao.exists(id, EntityLanguage))
                throw BadRequestException("Language doesn't exists")
            validateLanguageRequestOrThrowException(nativeId, languageId, languageName, flag)
            validateAdmin(user)
            val responseId = dataDao.updateLanguage(id, nativeId, languageId, languageName, flag)
            IntIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun deleteLanguage(user: User, languageId: Int): IntIdResponse {
        return try {
            checkDataExistsOrThrowException(languageId, EntityLanguage)
            validateAdmin(user)

            if (dataDao.deleteLanguage(languageId)) {
                IntIdResponse.success(languageId)
            } else {
                IntIdResponse.failed("Error Occurred")
            }
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            IntIdResponse.notFound(nfe.message)
        }
    }

    fun getUnit(unitId: Int, depth: DepthRequest): UnitResponse {
        return try {
            validateDepthOrThrowException(depth.depth)
            checkDataExistsOrThrowException(unitId, EntityUnit)
            val unit = dataDao.getUnitById(unitId, depth.depth)!!
            UnitResponse.success(Unit.create(unit))
        } catch (bre: BadRequestException) {
            UnitResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            UnitResponse.unauthorized(uae.message)
        } catch (nfe: NotFoundException) {
            UnitResponse.notFound(nfe.message)
        }
    }
    fun addUnit(user: User, unit: UnitRequest): IntIdResponse {
        return try {
            val title = unit.title
            val order  = unit.order
            val language = unit.languageId
            validateUnitRequestOrThrowException(title, order, language)
            validateAdmin(user)
            val responseId = dataDao.addUnit(title, order, language)
            IntIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun updateUnit(user: User, unitId: Int, unit: UpdateUnitRequest): IntIdResponse {
        return try {
            val title = unit.title
            val order  = unit.order
            if (!dataDao.exists(unitId, EntityUnit))
                throw BadRequestException("Unit doesn't exists")
            validateUpdateUnitRequestOrThrowException(title, order)
            validateAdmin(user)
            val responseId = dataDao.updateUnit(unitId, title, order)
            IntIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun deleteUnit(user: User, unitId: Int): IntIdResponse {
        return try {
            checkDataExistsOrThrowException(unitId, EntityUnit)
            validateAdmin(user)
            if (dataDao.deleteUnit(unitId)) {
                IntIdResponse.success(unitId)
            } else {
                IntIdResponse.failed("Error Occurred")
            }
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            IntIdResponse.notFound(nfe.message)
        }
    }

    fun getSection(sectionId: Int, depth: DepthRequest): SectionResponse {
        return try {
            validateDepthOrThrowException(depth.depth)
            checkDataExistsOrThrowException(sectionId, EntitySection)
            val section = dataDao.getSectionById(sectionId, depth.depth)!!
            SectionResponse.success(Section.create(section))
        } catch (bre: BadRequestException) {
            SectionResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            SectionResponse.unauthorized(uae.message)
        } catch (nfe: NotFoundException) {
            SectionResponse.notFound(nfe.message)
        }
    }
    fun addSection(user: User, section: SectionRequest): IntIdResponse {
        return try {
            val title = section.title
            val order = section.order
            val lessonCount = section.lessonCount
            val unit = section.unitId
            validateSectionRequestOrThrowException(title, order, lessonCount, unit)
            validateAdmin(user)
            val responseId = dataDao.addSection(title, order, lessonCount, unit)
            IntIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun updateSection(user: User, sectionId: Int, section: SectionRequest): IntIdResponse {
        return try {
            val title = section.title
            val order = section.order
            val lessonCount = section.lessonCount
            val unit = section.unitId
            if (!dataDao.exists(sectionId, EntitySection))
                throw BadRequestException("Section doesn't exists")
            validateUpdateSectionRequestOrThrowException(title, order, lessonCount, unit, sectionId)
            validateAdmin(user)
            val responseId = dataDao.updateSection(sectionId, title, order, lessonCount, unit)
            IntIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun deleteSection(user: User, sectionId: Int): IntIdResponse {
        return try {
            checkDataExistsOrThrowException(sectionId, EntitySection)
            validateAdmin(user)
            if (dataDao.deleteSection(sectionId)) {
                IntIdResponse.success(sectionId)
            } else {
                IntIdResponse.failed("Error Occurred")
            }
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            IntIdResponse.notFound(nfe.message)
        }
    }

    fun getWord(wordId: Int): WordResponse {
        return try {
            checkDataExistsOrThrowException(wordId, EntityWord)
            val word = dataDao.getWordById(wordId)!!
            WordResponse.success(Word.create(word))
        } catch (bre: BadRequestException) {
            WordResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            WordResponse.unauthorized(uae.message)
        } catch (nfe: NotFoundException) {
            WordResponse.notFound(nfe.message)
        }
    }
    fun addWord(user: User, word: WordRequest): IntIdResponse {
        return try {
            val phonic = word.phonic
            val sound = word.sound
            val translatedSound = word.translatedSound
            val translateWord = word.translatedWord
            val text = word.word
            val languageId = word.languageId
            validateWordRequestOrThrowException(phonic, sound, translatedSound, translateWord, text, languageId)
            validateAdmin(user)
            val responseId = dataDao.addWord(phonic, sound ,translatedSound, translateWord, text, languageId)
            IntIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun updateWord(user: User, wordId: Int, word: UpdateWordRequest): IntIdResponse {
        return try {
            val phonic = word.phonic
            val sound = word.sound
            val translatedSound = word.translatedSound
            val translateWord = word.translatedWord
            val text = word.word
            if (!dataDao.exists(wordId, EntityWord))
                throw BadRequestException("Word doesn't exist")
            validateUpdateWordRequestOrThrowException(wordId, phonic, sound, translatedSound, translateWord, text)
            validateAdmin(user)
            val responseId = dataDao.updateWord(wordId, phonic, sound ,translatedSound, translateWord, text)
            IntIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun deleteWord(user: User, wordId: Int): IntIdResponse {
        return try {
            checkDataExistsOrThrowException(wordId, EntityWord)
            validateAdmin(user)
            if (dataDao.deleteWord(wordId)) {
                IntIdResponse.success(wordId)
            } else {
                IntIdResponse.failed("Error Occurred")
            }
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            IntIdResponse.notFound(nfe.message)
        }
    }

    fun getSentence(sentenceId: Int): SentenceResponse {
        return try {
            checkDataExistsOrThrowException(sentenceId, EntitySentence)
            val sentence = dataDao.getSentenceById(sentenceId)!!
            SentenceResponse.success(Sentence.create(sentence))
        } catch (bre: BadRequestException) {
            SentenceResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            SentenceResponse.unauthorized(uae.message)
        } catch (nfe: NotFoundException) {
            SentenceResponse.notFound(nfe.message)
        }
    }
    fun addSentence(user: User, sentence: SentenceRequest): IntIdResponse {
        return try {
            val language = sentence.languageId
            val words = sentence.words
            validateSentenceRequestOrThrowException(language, words)
            validateAdmin(user)
            val responseId = dataDao.addSentence(language, words)
            IntIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun updateSentence(user: User, sentenceId: Int, sentence: UpdateSentenceRequest): IntIdResponse {
        return try {
            val words = sentence.words
            validateUpdateSentenceRequestOrThrowException(sentenceId, words)
            validateAdmin(user)
            val responseId = dataDao.updateSentence(sentenceId, words)
            IntIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun deleteSentence(user: User, sentenceId: Int): IntIdResponse {
        return try {
            validateAdmin(user)
            if (dataDao.deleteSentence(sentenceId)) {
                IntIdResponse.success(sentenceId)
            } else {
                IntIdResponse.failed("Error Occurred")
            }
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            IntIdResponse.notFound(nfe.message)
        }
    }

    fun addWordToSection(user: User, sectionId: Int, wordsRequest: WordSectionRequest): IntIdResponse {
        return try {
            val word = wordsRequest.word
            validateAdmin(user)
            if  (!dataDao.exists(sectionId, EntitySection)){
                throw BadRequestException("Section does not exist")
            }
            val section = dataDao.getSectionById(sectionId, SectionModel.SECTIONS_WITH_LESSON_DATA)!!
            val sectionWords = section.words.map {
                it.id
            }
            val sectionLanguage = dataDao.getUnitById(section.unit, UnitModel.UNIT)!!.language
            if (sectionWords.contains(word)){
                throw BadRequestException("Section already contains word")
            }
            if (!dataDao.exists(word, EntityWord)){
                throw BadRequestException("Word does not exist")

            } else if (dataDao.getWordById(word)!!.language != sectionLanguage){
                throw BadRequestException("Word language does not match section language")
            }
            dataDao.addWordToSection(word, sectionId)
            IntIdResponse.success(sectionId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun removeWordFromSection(user: User, sectionId: Int, wordsSection: WordSectionRequest): IntIdResponse{
        return try {
            val word = wordsSection.word
            validateAdmin(user)
            if  (!dataDao.exists(sectionId, EntitySection)){
                throw BadRequestException("Section does not exist")
            }
            val section = dataDao.getSectionById(sectionId, SectionModel.SECTIONS_WITH_LESSON_DATA)!!
            val sectionWords = section.words.map {
                it.id
            }
            if (!sectionWords.contains(word)){
                throw BadRequestException("Section does not contain word")
            }
            dataDao.removeWordFromSection(word, sectionId)
            IntIdResponse.success(sectionId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun addSentenceToSection(user: User, sectionId: Int, sentencesSection: SentenceSectionRequest): IntIdResponse{
        return try {
            val sentence = sentencesSection.sentence
            validateAdmin(user)
            if  (!dataDao.exists(sectionId, EntitySection)){
                throw BadRequestException("Section does not exist")
            }
            val section = dataDao.getSectionById(sectionId, SectionModel.SECTIONS_WITH_LESSON_DATA)!!
            val sectionSentences = section.sentences.map {
                it.id
            }
            val sectionLanguage = dataDao.getUnitById(section.unit, UnitModel.UNIT)!!.language
            if (sectionSentences.contains(sentence)){
                throw BadRequestException("Section already contains sentence")
            }
            if (!dataDao.exists(sentence, EntitySentence)){
                throw BadRequestException("Sentence does not exist")

            } else if (dataDao.getSentenceById(sentence)!!.language != sectionLanguage){
                throw BadRequestException("Sentence language does not match section language")
            }
            dataDao.addSentenceToSection(sentence, sectionId)
            IntIdResponse.success(sectionId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }
    fun removeSentenceFromSection(user: User, sectionId: Int, sentencesSection: SentenceSectionRequest): IntIdResponse{
        return try {
            val sentence = sentencesSection.sentence
            validateAdmin(user)
            if  (!dataDao.exists(sectionId, EntitySection)){
                throw BadRequestException("Section does not exist")
            }
            val section = dataDao.getSectionById(sectionId, SectionModel.SECTIONS_WITH_LESSON_DATA)!!
            val sectionSentences = section.sentences.map {
                it.id
            }
            if (!sectionSentences.contains(sentence)){
                throw BadRequestException("Section does not contain sentence")
            }
            dataDao.removeSentenceFromSection(sentence, sectionId)
            IntIdResponse.success(sectionId)
        } catch (bre: BadRequestException) {
            IntIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            IntIdResponse.unauthorized(uae.message)
        }
    }


    fun getAllFlags(): FlagResponse{
        val flags = dataDao.getAllFlags()
        return FlagResponse.success(flags.map { Flag.create(it)})
    }
    fun getFlag(flagId: String): FlagResponse {
        return try {
            if (!dataDao.flagExists(flagId)){
                throw BadRequestException("Flag does not exist")
            }
            val flag = dataDao.getFlagById(flagId)!!
            FlagResponse.success(Flag.create(flag))
        } catch (bre: BadRequestException) {
            FlagResponse.failed(bre.message)
        }
    }
    fun addFlag(user: User, flag: FlagRequest): StrIdResponse {
        return try {
            val img = flag.flag
            val id = flag.id
            validateFlagRequestOrThrowException(img, id)
            if (dataDao.flagExists(id))
                throw BadRequestException("Flag already exist")
            validateAdmin(user)
            val responseId = dataDao.addFlag(img, id)
            StrIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            StrIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            StrIdResponse.unauthorized(uae.message)
        }
    }

    fun updateFlag(user: User, flag: FlagRequest): StrIdResponse {
        return try {
            val img = flag.flag
            val id = flag.id
            validateFlagRequestOrThrowException(img, id)
            if (!dataDao.flagExists(id))
                throw BadRequestException("Flag doesn't exist")
            validateAdmin(user)
            val responseId = dataDao.updateFlag(id, img)
            StrIdResponse.success(responseId)
        } catch (bre: BadRequestException) {
            StrIdResponse.failed(bre.message)
        } catch (uae: UnauthorizedActivityException) {
            StrIdResponse.unauthorized(uae.message)
        }
    }
    fun deleteFlag(user: User, flagId: String): StrIdResponse {
        return try {
            validateAdmin(user)
            if (dataDao.deleteFlag(flagId)) {
                StrIdResponse.success(flagId)
            } else {
                StrIdResponse.failed("Error Occurred")
            }
        } catch (uae: UnauthorizedActivityException) {
            StrIdResponse.unauthorized(uae.message)
        } catch (bre: BadRequestException) {
            StrIdResponse.failed(bre.message)
        } catch (nfe: NotFoundException) {
            StrIdResponse.notFound(nfe.message)
        }
    }

    private fun validateDepthOrThrowException(depth: Int) {
        val message = when {
            depth < 0 -> "Depth does not exist"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateLanguageRequestOrThrowException(nativeId: String, languageId: String, languageName: String, flag: String){
        val message = when {
            nativeId.length != 2 -> "Native language id must be 2 characters"
            languageId.length != 2 -> "Language id must be 2 characters"
            flag.length != 2 -> "Flag id must be 2 characters"
            languageName.length > 30 -> "Language name is too long"
            !nativeId.containsOnlyLetters() -> "Native cannot contain numbers"
            !languageId.containsOnlyLetters() -> "Language cannot contain numbers"
            !languageName.containsOnlyLetters() -> "Language name cannot contain numbers"
            !dataDao.flagExists(flag) -> "Flag does not exist"
            dataDao.getLanguage(nativeId, languageId, 0) != null -> "Language already exists"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateUnitRequestOrThrowException(title: String, order: Int, languageId: Int){
        val message = when {
            !title.containsOnlyLetters()-> "'title' cannot contain numbers"
            title.length > 30 -> "'title' cannot contain more then 30 characters"
            order < 0 -> "'order' cannot be negative"
            !dataDao.exists(languageId, EntityLanguage) -> "Language does not exist"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateUpdateUnitRequestOrThrowException(title: String, order: Int){
        val message = when {
            !title.containsOnlyLetters()-> "'title' cannot contain numbers"
            title.length > 30 -> "'title' cannot contain more then 30 characters"
            order < 0 -> "'order' cannot be negative"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateSectionRequestOrThrowException(title: String, order: Int, lessonCount: Int, unitId: Int){
        val message = when {
            !title.containsOnlyLetters()-> "'title' cannot contain numbers"
            title.length > 30 -> "'title' cannot contain more then 30 characters"
            order < 0 -> "'order' cannot be negative"
            lessonCount < 1 -> "each section must contain at least one lesson"
            !dataDao.exists(unitId, EntityUnit) -> "Unit does not exist"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateUpdateSectionRequestOrThrowException(title: String, order: Int, lessonCount: Int, unitId: Int, sectionId: Int){
        val message = when {
            !title.containsOnlyLetters()-> "'title' cannot contain numbers"
            title.length > 30 -> "'title' cannot contain more then 30 characters"
            order < 0 -> "'order' cannot be negative"
            lessonCount < 1 -> "each section must contain at least one lesson"
            !dataDao.exists(unitId, EntityUnit) -> "Unit does not exist"
            dataDao.getUnitById(unitId, 0)!!.language != dataDao.getUnitById(dataDao.getSectionById(sectionId, 0)!!.unit, 0)!!.language -> "Section and Unit Language does not match"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateWordRequestOrThrowException(phonic: String, sound: String, translatedSound: String, translateWord: String, text: String, languageId: Int){
        val message = when {
            !phonic.containsOnlyLetters() -> "phonic cannot contain numbers"
            !text.containsOnlyLetters()  -> "word cannot contain numbers"
            !translateWord.containsOnlyLetters() -> "translated word cannot contain numbers"
            phonic.length > 100 || text.length > 100 || translateWord.length >100 -> "text is too long"
            sound.length > 300 || translatedSound.length > 300 -> "link text is too long"
            !dataDao.exists(languageId, EntityLanguage) -> "Language does not exist"
            dataDao.getWord(languageId, text) != null -> "Word already exists"
            else -> return
        }
        throw BadRequestException(message)
    }

    private fun validateUpdateWordRequestOrThrowException(wordId: Int, phonic: String, sound: String, translatedSound: String, translateWord: String, text: String){
        val message = when {
            !phonic.containsOnlyLetters() -> "phonic cannot contain numbers"
            !text.containsOnlyLetters()  -> "word cannot contain numbers"
            !translateWord.containsOnlyLetters() -> "translated word cannot contain numbers"
            phonic.length > 100 || text.length > 100 || translateWord.length >100 -> "text is too long"
            sound.length > 300 || translatedSound.length > 300 -> "link text is too long"
            dataDao.getWord(dataDao.getWordById(wordId)!!.language, text) != null -> "Word already exists"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateSentenceRequestOrThrowException(languageId: Int, words: List<Int>){
        val message = when {
            words.any {!dataDao.exists(it, EntityWord)} -> "Word does not exist"
            !dataDao.exists(languageId, EntityLanguage) -> "Language does not exist"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateUpdateSentenceRequestOrThrowException(sentenceId: Int, words: List<Int>){
        val message = when {
            words.any {!dataDao.exists(it, EntityWord)} -> "Word does not exist"
            words.any {dataDao.getWordById(it)!!.language != dataDao.getSentenceById(sentenceId)!!.language} -> "Word language does not match sentence language"
            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateFlagRequestOrThrowException(img: String, id: String){
        val message = when {
            id.length != 2 -> "Id must be 2 characters"
            !id.containsOnlyLetters() -> "Id cannot contain numbers"

            else -> return
        }
        throw BadRequestException(message)
    }
    private fun validateAdmin(user: User){
        val message = when {
            !user.isAdmin -> "You must be an admin to add languages"
            else -> return
        }
        throw UnauthorizedActivityException(message)
    }
    private fun checkDataExistsOrThrowException(dataId: Int, type: Any) {
        if (!dataDao.exists(dataId, type)) {
            throw NotFoundException("Data not exist with ID '$dataId'")
        }
    }
    private fun checkNativeExistsOrThrowException(native: String) {
        val message = when {
            native.length != 2 -> "Native language id must be 2 characters"
            !dataDao.nativeExists(native) -> "Native id does not exist"
            else -> return
        }
        throw BadRequestException(message)
    }


}