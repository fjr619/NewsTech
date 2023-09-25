package com.fjr619.newsloc.di

import android.app.Application
import androidx.room.Room
import com.fjr619.newsloc.data.local.NewsDao
import com.fjr619.newsloc.data.local.NewsDatabase
import com.fjr619.newsloc.data.local.NewsTypeConvertor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//object DatabaseModule {
//
//}