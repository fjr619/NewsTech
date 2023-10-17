package com.fjr619.newsloc.presentation.detail

import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.domain.model.Source
import com.fjr619.newsloc.util.UiText
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class DetailViewState(
  val article: Article = Article(
    author = "",
    content = "",
    publishedAt = "",
    source = Source("", ""),
    title = "",
    url = "",
  ),
  val bookmarkArticle: Article? = null,
  val processSucessEvent: StateEventWithContent<UiText> = consumed()
)