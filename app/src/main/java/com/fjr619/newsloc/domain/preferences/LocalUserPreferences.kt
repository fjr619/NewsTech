package com.fjr619.newsloc.domain.preferences

import kotlinx.coroutines.flow.Flow

interface LocalUserPreferences {
    suspend fun saveAppEntry()

    fun readAppEntry(): Flow<Boolean>
}