package com.openphonics.common.di.component

import com.openphonics.auth.UserDao
import com.openphonics.common.di.module.DAOModule
import dagger.Lazy
import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [DAOModule::class])
interface DaoComponent {
    fun userDao(): Lazy<UserDao>
}