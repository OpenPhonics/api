package com.openphonics.common.di.module

import dagger.Binds
import dagger.Module
import com.openphonics.auth.Encryptor
import com.openphonics.auth.OpEncryptor
import javax.inject.Singleton

@Module
interface EncryptorModule {
    @Singleton
    @Binds
    abstract fun encryptor(encryptor: OpEncryptor): Encryptor
}