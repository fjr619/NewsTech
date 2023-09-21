package com.fjr619.newsloc.domain.usecase.appentry

import com.fjr619.newsloc.domain.preferences.LocalUserPreferences
import javax.inject.Inject

class SaveAppEntry @Inject constructor(
    private val localUserPreferences: LocalUserPreferences
) {
    suspend operator fun invoke() {
        localUserPreferences.saveAppEntry()
    }
}