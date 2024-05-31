package com.fjr619.newsloc.presentation.search

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.paging.compose.collectAsLazyPagingItems
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.Dimens.ExtraSmallPadding2
import com.fjr619.newsloc.presentation.Dimens.MediumPadding1
import com.fjr619.newsloc.presentation.common.ArticlesList
import com.fjr619.newsloc.presentation.common.SearchBar

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SearchScreen(
    state: SearchState,
    event: (SearchEvent) -> Unit,
    navigateToDetail: (Article) -> Unit,
) {

    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier
                    .padding(horizontal = MediumPadding1)
                    .padding(top = ExtraSmallPadding2)
                    .statusBarsPadding(),
                text = state.searchQuery,
                readOnly = false,
                onValueChange = { event(SearchEvent.UpdateSearchQuery(it)) },
                onSearch = {
                    event(SearchEvent.SearchNews)
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Spacer(modifier = Modifier.height(MediumPadding1))

            state.articles?.let {
                val articles = it.collectAsLazyPagingItems()
                ArticlesList(
                    articles = articles,
                    onClickCard = navigateToDetail,
                )
            }
        }
    }



}