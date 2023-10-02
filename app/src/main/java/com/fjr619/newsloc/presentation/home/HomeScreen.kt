package com.fjr619.newsloc.presentation.home

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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import com.fjr619.newsloc.R
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.navgraph.Route
import com.fjr619.newsloc.presentation.Dimens.MediumPadding1
import com.fjr619.newsloc.presentation.common.ArticlesList
import com.fjr619.newsloc.presentation.common.SearchBar
import com.fjr619.newsloc.ui.theme.customColorsPalette

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    articles: LazyPagingItems<Article>,
    navigateToSearch: () -> Unit,
    navigateToDetail: (Article) -> Unit
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
                    navigateToSearch()
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

            ArticlesList(
                articles = articles,
                onClickCard = navigateToDetail
            )
        }
    }

}