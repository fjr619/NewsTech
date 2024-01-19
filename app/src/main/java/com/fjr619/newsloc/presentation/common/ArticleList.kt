package com.fjr619.newsloc.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.Dimens.MediumPadding1
import com.fjr619.newsloc.presentation.home.components.ArticleCard
import com.fjr619.newsloc.util.pulltorefresh.PullToRefreshLayoutState
import com.fjr619.newsloc.util.pulltorefresh.RefreshIndicatorState

@Composable
fun ArticlesList2(
    modifier: Modifier = Modifier,
    articles: List<Article>,
    onClickCard: (Article) -> Unit
) {
    if (articles.isEmpty()){
        EmptyScreen()
    }
    LazyColumn(
        modifier = modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.spacedBy(MediumPadding1),
//        contentPadding = PaddingValues(all = ExtraSmallPadding2)
    ) {
        items(
            count = articles.size,
        ) {
            articles[it]?.let { article ->
                ArticleCard(article = article, onClick = { onClickCard(article) })
            }
        }
    }

}

@Composable
fun ArticlesList(
    modifier: Modifier = Modifier,
    articles: LazyPagingItems<Article>,
    pullToRefreshLayoutState: PullToRefreshLayoutState,
    onClickCard: (Article) -> Unit
) {

    val handlePagingResult = handlePagingResult(articles, pullToRefreshLayoutState)


    if (handlePagingResult) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(MediumPadding1),
        ) {
            items(
                count = articles.itemCount,
                key = articles.itemKey()
            ) {
                articles[it]?.let { article ->
                    ArticleCard(
                        article = article, onClick = { onClickCard(article) })
                }
            }
        }
    }
}

@Composable
fun handlePagingResult(articles: LazyPagingItems<Article>, pullToRefreshLayoutState: PullToRefreshLayoutState): Boolean {
    val loadState = articles.loadState
    val refreshIndicatorState by pullToRefreshLayoutState.refreshIndicatorState.collectAsStateWithLifecycle()
    val error = when {
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        else -> null
    }

    return when {
        loadState.refresh is LoadState.Loading -> {
            if (refreshIndicatorState == RefreshIndicatorState.Default){
                ShimmerEffect()
                false
            } else{
                true
            }
        }

        error != null -> {
            EmptyScreen(error = error)
            pullToRefreshLayoutState.updateRefreshState(RefreshIndicatorState.Default)
            false
        }

        else -> {
            pullToRefreshLayoutState.updateRefreshState(RefreshIndicatorState.Default)
            true
        }
    }
}

@Composable
fun ShimmerEffect() {
    Column(verticalArrangement = Arrangement.spacedBy(MediumPadding1)) {
        repeat(10) {
            ArticleCardShimmerEffect(
                modifier = Modifier.padding(horizontal = MediumPadding1)
            )
        }
    }
}