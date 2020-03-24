package com.annalisetarhan.kacaolur.dagger

import com.annalisetarhan.kacaolur.storage.SharedPreferencesStorage
import com.annalisetarhan.kacaolur.storage.Storage
import dagger.Binds
import dagger.Module

@Module
abstract class StorageModule {
    @Binds
    abstract fun provideStorage(storage: SharedPreferencesStorage): Storage
}