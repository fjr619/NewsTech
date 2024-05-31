package com.fjr619.newsloc.presentation.bookmark

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.Dimens.MediumPadding1
import com.fjr619.newsloc.presentation.common.ArticlesListBookmark

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun BookmarkScreen(
    state: BookmarkState,
    navigateToDetails: (Article) -> Unit,
    onDelete: (Article) -> Unit
) {

    Scaffold(
        topBar = {
            Text(
                modifier = Modifier
                    .padding(horizontal = MediumPadding1)
                    .statusBarsPadding(),
                text = "Bookmark",
                style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
//            color = MaterialTheme.customColorsPalette.textTitle
            )
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

//        Spacer(modifier = Modifier.height(16.dp))

            ArticlesListBookmark(
                articles = state.articles,
                onClickCard = navigateToDetails,
                onDelete = onDelete
            )
        }
    }


}