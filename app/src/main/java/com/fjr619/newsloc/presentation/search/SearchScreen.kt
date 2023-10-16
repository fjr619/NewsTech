package com.fjr619.newsloc.presentation.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.Dimens.MediumPadding1
import com.fjr619.newsloc.presentation.common.ArticlesList
import com.fjr619.newsloc.presentation.common.SearchBar
import com.fjr619.newsloc.presentation.navgraph.SearchNavGraph
import com.ramcosta.composedestinations.annotation.Destination

@SearchNavGraph(
    start = true
)
@Destination
@Composable
fun SearchScreen(
    state: SearchState,
    event: (SearchEvent) -> Unit,
    navigateToDetail: (Article) -> Unit,
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        SearchBar(
            modifier = Modifier.padding(
                top = MediumPadding1,
                start = MediumPadding1,
                end = MediumPadding1
            ),
            text = state.searchQuery,
            readOnly = false,
            onValueChange = { event(SearchEvent.UpdateSearchQuery(it)) },
            onSearch = {
                event(SearchEvent.SearchNews)
            }
        )
        Spacer(modifier = Modifier.height(MediumPadding1))
        state.articles?.let {
            val articles = it.collectAsLazyPagingItems()
            ArticlesList(
                articles = articles,
                onClickCard = navigateToDetail
            )
        }
    }

}