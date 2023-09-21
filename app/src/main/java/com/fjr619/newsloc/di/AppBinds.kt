package com.fjr619.newsloc.di

import com.fjr619.newsloc.data.preferences.LocalUserPreferencesImpl
import com.fjr619.newsloc.data.repository.NewsRepositoryImpl
import com.fjr619.newsloc.domain.preferences.LocalUserPreferences
import com.fjr619.newsloc.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppBinds {

    @Binds
    abstract fun bindLocalUserPreferences(
        localUserPreferencesImpl: LocalUserPreferencesImpl
    ): LocalUserPreferences

    @Binds
    abstract fun bindNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository
}