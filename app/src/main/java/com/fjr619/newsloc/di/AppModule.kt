package com.fjr619.newsloc.di

import com.fjr619.newsloc.data.remote.NewsApi
import com.fjr619.newsloc.domain.usecase.appentry.AppEntryUseCases
import com.fjr619.newsloc.domain.usecase.appentry.ReadAppEntry
import com.fjr619.newsloc.domain.usecase.appentry.SaveAppEntry
import com.fjr619.newsloc.domain.usecase.news.DeleteArticle
import com.fjr619.newsloc.domain.usecase.news.GetArticle
import com.fjr619.newsloc.domain.usecase.news.GetBookmarks
import com.fjr619.newsloc.domain.usecase.news.GetNews
import com.fjr619.newsloc.domain.usecase.news.NewsUseCases
import com.fjr619.newsloc.domain.usecase.news.SearchNews
import com.fjr619.newsloc.domain.usecase.news.UpsertArticle
import com.fjr619.newsloc.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiInstance(): NewsApi {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        return Retrofit
            .Builder()
            .client(client)
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
        getBookmarks: GetBookmarks,
        getArticle: GetArticle,
        upsertArticle: UpsertArticle,
    ) = NewsUseCases(
        getNews = getNews,
        searchNews = searchNews,
        deleteArticle = deleteArticle,
        getArticle = getArticle,
        getBookmarks = getBookmarks,
        upsertArticle = upsertArticle
    )
}

//@InstallIn(ActivityComponent::class) // << Activity scoped
//@Module
//object InternalObjectModule {
//    @Provides
//    @ActivityScoped
//    fun provideHomeManager(@ActivityContext context: Context) = BiometricPromptManager(context)
//}