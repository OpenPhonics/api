package com.openphonics.application.di.component
import com.openphonics.application.di.module.DaoModule
import com.openphonics.application.di.module.MapperModule
//import com.openphonics.application.di.module.MapperModule
import com.openphonics.application.di.module.ValidatorModule
import com.openphonics.data.flag.FlagDao
import com.openphonics.data.language.LanguageDao
import com.openphonics.data.word.WordDao
import dagger.Lazy
import dagger.Subcomponent
import io.ktor.util.*
import javax.inject.Singleton

@KtorExperimentalAPI
@Singleton
@Subcomponent(modules = [DaoModule::class, MapperModule::class])
interface DaoComponent {
    fun flagDao(): Lazy<FlagDao>
    fun languageDao(): Lazy<LanguageDao>
    fun wordDao(): Lazy<WordDao>
}