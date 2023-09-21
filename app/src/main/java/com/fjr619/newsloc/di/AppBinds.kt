package com.fjr619.newsloc.di

import com.fjr619.newsloc.data.preferences.LocalUserPreferencesImpl
import com.fjr619.newsloc.domain.preferences.LocalUserPreferences
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
}