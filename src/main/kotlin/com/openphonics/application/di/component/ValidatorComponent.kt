package com.openphonics.application.di.component

import com.openphonics.application.di.module.DaoModule
import com.openphonics.application.di.module.ValidatorModule
import com.openphonics.utils.OpenPhonicsRequestValidator
import dagger.Lazy
import dagger.Subcomponent
import io.ktor.util.*
import javax.inject.Singleton

@KtorExperimentalAPI
@Singleton
@Subcomponent(modules = [DaoModule::class, ValidatorModule::class])
interface ValidatorComponent {
    fun validator(): Lazy<OpenPhonicsRequestValidator>
}