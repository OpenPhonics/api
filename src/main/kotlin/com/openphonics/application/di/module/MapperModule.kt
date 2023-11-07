package com.openphonics.application.di.module

import com.openphonics.utils.OpenPhonicsMapper
import com.openphonics.utils.OpenPhonicsMapperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface MapperModule {
    @Singleton
    @Binds
    fun mapper(mapper: OpenPhonicsMapperImpl): OpenPhonicsMapper
}