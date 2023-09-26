package com.fjr619.newsloc.di

import android.app.Application
import androidx.room.Room
import com.fjr619.newsloc.data.local.NewsDao
import com.fjr619.newsloc.data.local.NewsDatabase
import com.fjr619.newsloc.data.local.NewsTypeConvertor
import com.fjr619.newsloc.data.remote.NewsApi
import com.fjr619.newsloc.domain.usecase.appentry.AppEntryUseCases
import com.fjr619.newsloc.domain.usecase.appentry.ReadAppEntry
import com.fjr619.newsloc.domain.usecase.appentry.SaveAppEntry
import com.fjr619.newsloc.domain.usecase.news.DeleteArticle
import com.fjr619.newsloc.domain.usecase.news.GetArticle
import com.fjr619.newsloc.domain.usecase.news.GetArticles
import com.fjr619.newsloc.domain.usecase.news.GetNews
import com.fjr619.newsloc.domain.usecase.news.NewsUseCases
import com.fjr619.newsloc.domain.usecase.news.SearchNews
import com.fjr619.newsloc.domain.usecase.news.UpsertArticle
import com.fjr619.newsloc.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiInstance(): NewsApi {
        return Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAppEntryUsecases(
        readAppEntry: ReadAppEntry,
        saveAppEntry: SaveAppEntry
    ) = AppEntryUseCases(
        readAppEntry = readAppEntry,
        saveAppEntry = saveAppEntry
    )

    @Provides
    @Singleton
    fun provideNewsUsecases(
        getNews: GetNews,
        searchNews: SearchNews,
        deleteArticle: DeleteArticle,
        getArticles: GetArticles,
        getArticle: GetArticle,
        upsertArticle: UpsertArticle,
    ) = NewsUseCases(
        getNews = getNews,
        searchNews = searchNews,
        deleteArticle = deleteArticle,
        getArticle = getArticle,
        getArticles = getArticles,
        upsertArticle = upsertArticle
    )
}