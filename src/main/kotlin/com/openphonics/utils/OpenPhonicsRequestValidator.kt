package com.openphonics.utils
import com.openphonics.application.exception.BadRequestException
import com.openphonics.application.request.*
import com.openphonics.data.flag.FlagDao
import com.openphonics.data.language.LanguageDao
import com.openphonics.data.word.WordDao
import javax.inject.Inject
import javax.inject.Singleton

interface OpenPhonicsRequestValidator {
    fun flagExistsOrThrowException(id: String)
    fun languageExistsOrThrowException(id: Int)
    fun wordExistsOrThrowException(id: Int)
    fun validateOrThrowException(request: FlagRequest)
    fun validateOrThrowException(id: String, request: UpdateFlagRequest)
    fun validateOrThrowException(request: LanguageRequest)
    fun validateOrThrowException(id: Int, request: UpdateLanguageRequest)
    fun validateOrThrowException(request: WordRequest)
    fun validateOrThrowException(id: Int, request: UpdateWordRequest)

}
@Singleton
class OpenPhonicsRequestValidatorImpl @Inject constructor(
    private val flagDao: FlagDao,
    private val languageDao: LanguageDao,
    private val wordDao: WordDao,
    ) : OpenPhonicsRequestValidator {
    override fun flagExistsOrThrowException(id: String){
        if (!flagDao.exists(id)){
            throw BadRequestException("Flag does not exist")
        }
    }
    override fun languageExistsOrThrowException(id: Int){
        if (!languageDao.exists(id)){
            throw BadRequestException("Language does not exist")
        }
    }
    override fun wordExistsOrThrowException(id: Int){
        if (!wordDao.exists(id)){
            throw BadRequestException("Word does not exist")
        }
    }
    override fun validateOrThrowException(request: FlagRequest){
        with(request){
            val message = when {
                id.length != 2 -> "Id must be 2 characters"
                !id.containsOnlyLetters() -> "Id cannot contain numbers"
                flagDao.exists(id)-> "Flag already exists"
                else -> return
            }
            throw BadRequestException(message)
        }
    }
    override fun validateOrThrowException(id: String, request: UpdateFlagRequest){
        flagExistsOrThrowException(id)
    }
    override fun validateOrThrowException(request: LanguageRequest){
        with(request){
            val message = when {
                nativeId.length != 2 -> "Native language id must be 2 characters"
                languageId.length != 2 -> "Language id must be 2 characters"
                flag.length != 2 -> "Flag id must be 2 characters"
                languageName.length > 30 -> "Language name is too long"
                !nativeId.containsOnlyLetters() -> "Native cannot contain numbers"
                !languageId.containsOnlyLetters() -> "Language cannot contain numbers"
                !languageName.containsOnlyLetters() -> "Language name cannot contain numbers"
                !flagDao.exists(flag) -> "Flag does not exist"
                languageDao.exists(nativeId, languageId) -> "Language already exists"
                else -> return
            }
            throw BadRequestException(message)
        }
    }
    override fun validateOrThrowException(id: Int, request: UpdateLanguageRequest){
        languageExistsOrThrowException(id)
        val languageData = languageDao.get(id)!!
        with(request){
            val message = when {
                nativeId != null && nativeId.length != 2 -> "Native language id must be 2 characters"
                languageId != null && languageId.length != 2 -> "Language id must be 2 characters"
                flag != null && flag.length != 2 -> "Flag id must be 2 characters"
                languageName != null && languageName.length > 30 -> "Language name is too long"
                nativeId != null && !nativeId.containsOnlyLetters() -> "Native cannot contain numbers"
                languageId != null &&  !languageId.containsOnlyLetters() -> "Language cannot contain numbers"
                languageName != null &&  !languageName.containsOnlyLetters() -> "Language name cannot contain numbers"
                flag != null && !flagDao.exists(flag) -> "Flag does not exist"
                nativeId == null && languageId == null -> return
                languageDao.exists(nativeId ?: languageData.nativeId, languageId ?: languageData.languageId) -> "Language already exists"
                else -> return
            }
            throw BadRequestException(message)
        }
    }
    override fun validateOrThrowException(request: WordRequest){
        with(request){
            val message = when {
                !phonic.containsOnlyLetters() -> "phonic cannot contain numbers"
                !word.containsOnlyLetters()  -> "word cannot contain numbers"
                !translatedWord.containsOnlyLetters() -> "translated word cannot contain numbers"
                phonic.length > 100 || word.length > 100 || translatedWord.length >100 -> "text is too long"
                sound.length > 300 || translatedSound.length > 300 -> "link text is too long"
                !languageDao.exists(language) -> "Language does not exist"
                wordDao.exists(language, word) -> "Word already exists"
                else -> return
            }
            throw BadRequestException(message)
        }
    }
    override fun validateOrThrowException(id: Int, request: UpdateWordRequest){
        with(request){
            val wordData = wordDao.get(id)!!
            val message = when {
                phonic != null && !phonic.containsOnlyLetters() -> "phonic cannot contain numbers"
                word != null && !word.containsOnlyLetters()  -> "word cannot contain numbers"
                translatedWord != null && !translatedWord.containsOnlyLetters() -> "translated word cannot contain numbers"
                (phonic != null && phonic.length > 100) || (word != null && word.length > 100) || (translatedWord != null && translatedWord.length >100) -> "text is too long"
                (sound != null && sound.length > 300) || (translatedSound != null && translatedSound.length > 300) -> "link text is too long"
                word != null && wordDao.exists(wordData.language, word) -> "Word already exists"
                else -> return
            }
            throw BadRequestException(message)
        }
    }
}