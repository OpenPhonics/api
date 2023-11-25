package com.openphonics.common.core

interface CRUD<Template, Create : Template, Update : Template, Base, Id, Conditional> {
    fun get(id: Int): Base
    fun create(data: Create): Id
    fun update(id: Int, data: Update): Id
    fun delete(id: Int): Conditional
}