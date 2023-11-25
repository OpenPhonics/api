package com.openphonics.common.core

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.transactions.transaction

typealias CRUDDAO<Template, Create, Update, Base> = CRUD<Template, Create, Update, Base?, Int, Boolean>

abstract class DAO<Template, Create : Template, Update : Template, Base : Template, Entity : IntEntity>(
    protected val table: IntEntityClass<Entity>
) : CRUDDAO<Template, Create, Update, Base> {
    abstract override fun create(data: Create): Int
    abstract override fun update(id: Int, data: Update): Int
    abstract fun convert(entity: Entity): Base
    override fun get(id: Int): Base? = transaction {
        table.findById(id)?.let { convert(it) }
    }
    override fun delete(id: Int): Boolean = transaction {
        table.findById(id)?.run{
            delete()
            return@transaction true
        }
        return@transaction false
    }
}