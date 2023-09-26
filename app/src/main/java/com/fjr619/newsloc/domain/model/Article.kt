package com.fjr619.newsloc.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import javax.annotation.Nullable

@Parcelize
@Entity(tableName = "Article")
data class Article(
    val author: String,
    val content: String,
    val description: String? = null,
    val publishedAt: String,
    val source: Source,
    val title: String,
    @PrimaryKey val url: String,
    val urlToImage: String? = null
): Parcelable