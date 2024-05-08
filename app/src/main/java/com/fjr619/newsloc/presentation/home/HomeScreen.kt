package com.fjr619.newsloc.presentation.home

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import com.fjr619.newsloc.R
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.Dimens.MediumPadding1
import com.fjr619.newsloc.presentation.common.ArticlesList
import com.fjr619.newsloc.presentation.common.SearchBar
import com.fjr619.newsloc.presentation.navgraph.MaterialNavScreen
import com.fjr619.newsloc.ui.theme.customColorsPalette
import com.fjr619.newsloc.util.pulltorefresh.PullToRefreshLayout
import com.fjr619.newsloc.util.pulltorefresh.PullToRefreshLayoutState
import kotlinx.coroutines.delay

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalFoundationApi::class)
@Composable
fun SharedTransitionScope.HomeScreen(
    paddingValues: PaddingValues,
    articles: LazyPagingItems<Article>,
    navigateToSearch: (MaterialNavScreen) -> Unit,
    navigateToDetail: (Article) -> Unit,
    onExitApp: () -> Unit,
    pullToRefreshLayoutState: PullToRefreshLayoutState,
    onRefresh: ()->Unit
) {

    val titles by remember {
        derivedStateOf {
            if (articles.itemCount > 10) {
                articles.itemSnapshotList.items
                    .slice(IntRange(start = 0, endInclusive = 9))
                    .joinToString(separator = " \uD83D\uDCF0 ") { it.title }
            } else {
                ""
            }
        }
    }

    var exit by remember { mutableStateOf(false) }
    val context = LocalContext.current

    //https://stackoverflow.com/a/77613696
    //handle double back press for exit application
    LaunchedEffect(key1 = exit) {
        if (exit) {
            delay(2000)
            exit = false
        }
    }

    BackHandler {
        if (exit) {
            onExitApp()
        } else {
            exit = true
            Toast.makeText(context, "Press again to exit", Toast.LENGTH_SHORT).show()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
//            .padding(top = Dimens.MediumPadding1)
//                .safeDrawingPadding()
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = null,
                modifier = Modifier
                    .width(150.dp)
                    .height(30.dp)
                    .padding(horizontal = MediumPadding1)
            )

            Spacer(modifier = Modifier.height(MediumPadding1))

            SearchBar(
                modifier = Modifier
                    .padding(horizontal = MediumPadding1)
                    .fillMaxWidth(),
                text = "",
                readOnly = true,
                onValueChange = {},
                onSearch = {},
                onClick = {
                    navigateToSearch(MaterialNavScreen.Search)
                }
            )

            Spacer(modifier = Modifier.height(MediumPadding1))

            Text(
                text = titles, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = MediumPadding1)
                    .basicMarquee(), fontSize = 12.sp,
                color = MaterialTheme.customColorsPalette.placeholder
            )

            Spacer(modifier = Modifier.height(MediumPadding1))

            PullToRefreshLayout(
                modifier = Modifier.fillMaxSize(),
                pullRefreshLayoutState = pullToRefreshLayoutState,
                onRefresh = onRefresh,
            ) {
                ArticlesList(
                    prefixSharedKey = "home",
                    articles = articles,
                    pullToRefreshLayoutState = pullToRefreshLayoutState,
                    onClickCard = navigateToDetail
                )
            }
        }
    }

}