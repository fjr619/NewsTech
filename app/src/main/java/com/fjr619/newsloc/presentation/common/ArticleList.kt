package com.fjr619.newsloc.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.Dimens.MediumPadding1
import com.fjr619.newsloc.presentation.home.components.ArticleCard
import com.fjr619.newsloc.util.pulltorefresh.PullToRefreshLayoutState
import com.fjr619.newsloc.util.pulltorefresh.RefreshIndicatorState
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ArticlesList2(
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
            articles[it].run {
                ArticleCard(article = this, onClick = { onClickCard(this) })
            }
        }
    }

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ArticlesList(
    modifier: Modifier = Modifier,
    articles: LazyPagingItems<Article>,
    pullToRefreshLayoutState: PullToRefreshLayoutState? = null,
    onClickCard: (Article) -> Unit
) {

    val handlePagingResult = handlePagingResult(articles, pullToRefreshLayoutState)
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

//    //Here we create a condition if the firstVisibleItemIndex is greater than 0
//    val showButton by remember {
//        derivedStateOf {
//            listState.firstVisibleItemIndex > 0
//        }
//    }

    if (handlePagingResult) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = listState
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

        AnimatedVisibility(visible = !listState.isScrollingUp(), enter = fadeIn(), exit = fadeOut()) {
            GoToTop {
                scope.launch {
                    listState.scrollToItem(0)
                }
            }
        }
    }
}

@Composable
fun handlePagingResult(articles: LazyPagingItems<Article>, pullToRefreshLayoutState: PullToRefreshLayoutState?): Boolean {
    val loadState = articles.loadState
    val refreshIndicatorState = pullToRefreshLayoutState?.refreshIndicatorState?.collectAsStateWithLifecycle()
    val error = when {
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        else -> null
    }

    return when {
        loadState.refresh is LoadState.Loading -> {
            if (refreshIndicatorState?.value == RefreshIndicatorState.Default || pullToRefreshLayoutState == null){
                ShimmerEffect()
                false
            } else{
                true
            }
        }

        error != null -> {
            EmptyScreen(error = error)
            pullToRefreshLayoutState?.updateRefreshState(RefreshIndicatorState.Default)
            false
        }

        else -> {
            pullToRefreshLayoutState?.updateRefreshState(RefreshIndicatorState.Default)
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


@Composable
fun GoToTop(goToTop: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .padding(16.dp)
                .size(50.dp)
                .align(Alignment.BottomEnd),
            onClick = goToTop,
            containerColor = White, contentColor = Black,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Outlined.KeyboardArrowUp,
                contentDescription = "go to top"
            )
        }
    }
}

@Composable
fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by rememberSaveable(this) { mutableIntStateOf(firstVisibleItemIndex) }
    var previousScrollOffset by rememberSaveable(this) { mutableIntStateOf(firstVisibleItemScrollOffset) }

    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}