package com.openphonics.data.flag

import com.openphonics.utils.OpenPhonicsMapper
import org.jetbrains.exposed.sql.transactions.transaction
import javax.inject.Inject
import javax.inject.Singleton

interface FlagDao {
    fun create(id: String, flag: String): String
    fun all(): List<Flag>
    fun update(id: String, newFlag: String): String
    fun get(id: String): Flag?
    fun delete(id: String): Boolean
    fun exists(id: String): Boolean
}
@Singleton
class FlagDaoImpl @Inject constructor(
    private val mapper: OpenPhonicsMapper
) : FlagDao {
    override fun create(id: String, flag: String): String = transaction {
        FlagEntity.new(id) {
            this.flag = flag
        }.id.value
    }
    override fun all(): List<Flag> = transaction {
        FlagEntity.all().map {mapper.fromEntity(it)}
    }
    override fun update(id: String, newFlag: String): String = transaction {
        FlagEntity[id].apply {
            this.flag = newFlag
        }.id.value
    }

    override fun get(id: String): Flag? = transaction {
        FlagEntity.findById(id)?.let {mapper.fromEntity(it)}
    }

    override fun delete(id: String): Boolean = transaction {
        FlagEntity.findById(id)?.run {
            delete()
            return@transaction true
        }
        return@transaction false
    }
    override fun exists(id: String) = get(id) != null
}