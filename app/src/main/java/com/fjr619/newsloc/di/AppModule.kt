package com.fjr619.newsloc.di

import android.app.Application
import com.fjr619.newsloc.data.preferences.LocalUserPreferencesImpl
import com.fjr619.newsloc.domain.preferences.LocalUserPreferences
import com.fjr619.newsloc.domain.usecase.AppEntryUseCases
import com.fjr619.newsloc.domain.usecase.ReadAppEntry
import com.fjr619.newsloc.domain.usecase.SaveAppEntry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppEntryUsecases(
        readAppEntry: ReadAppEntry,
        saveAppEntry: SaveAppEntry
    ) = AppEntryUseCases(
        readAppEntry = readAppEntry,
        saveAppEntry = saveAppEntry
    )
}