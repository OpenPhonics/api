package com.openphonics.language

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Languages : IntIdTable() {
    val languageCode = char("language_code", length = 2).uniqueIndex()
    val languageName = varchar("language_name", length = 30).uniqueIndex()
    val countryCode = char("country_code", length = 2)

}
class LanguageEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<LanguageEntity>(Languages)

    var languageCode by Languages.languageCode
    var languageName by Languages.languageName
    var countryCode by Languages.countryCode
}
@Serializable
sealed class LanguageTemplate {
    abstract val languageCode: String?
    abstract val languageName: String?
    abstract val countryCode: String?
}
@Serializable
data class LanguageCreate(
    override val languageCode: String,
    override val languageName: String,
    override val countryCode: String
) : LanguageTemplate()
@Serializable
data class LanguageUpdate(
    override val languageCode: String,
    override val languageName: String,
    override val countryCode: String
) : LanguageTemplate()
@Serializable
data class LanguageBase(
    val id: Int,
    override val languageCode: String,
    override val languageName: String,
    override val countryCode: String
) : LanguageTemplate()

