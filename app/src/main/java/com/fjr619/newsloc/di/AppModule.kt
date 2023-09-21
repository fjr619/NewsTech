package com.fjr619.newsloc.di

import com.fjr619.newsloc.domain.usecase.appentry.AppEntryUseCases
import com.fjr619.newsloc.domain.usecase.appentry.ReadAppEntry
import com.fjr619.newsloc.domain.usecase.appentry.SaveAppEntry
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