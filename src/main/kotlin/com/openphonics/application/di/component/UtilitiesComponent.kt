package com.openphonics.application.di.component

import com.openphonics.application.di.module.DaoModule
import com.openphonics.utils.OpenPhonicsMapper
import com.openphonics.utils.OpenPhonicsRequestValidator
import dagger.Lazy
import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [DaoModule::class])
interface UtilitiesComponent {
    fun mapper(): Lazy<OpenPhonicsMapper>
    fun validator(): Lazy<OpenPhonicsRequestValidator>
}