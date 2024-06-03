package com.fjr619.newsloc.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fjr619.newsloc.domain.model.Article
import kotlinx.coroutines.flow.Flow
@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article)

    @Delete
    suspend fun delete(article: Article)

    @Query("SELECT * FROM Article")
    fun getArticles(): PagingSource<Int, Article>

    @Query("SELECT * FROM Article WHERE url=:url")
    suspend fun getArticle(url: String): Article?
}