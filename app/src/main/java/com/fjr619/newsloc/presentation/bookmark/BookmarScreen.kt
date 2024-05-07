package com.fjr619.newsloc.presentation.bookmark

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.Dimens.MediumPadding1
import com.fjr619.newsloc.presentation.common.ArticlesListBookmark

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.BookmarkScreen(
    paddingValues: PaddingValues,
    state: BookmarkState,
    navigateToDetails: (Article) -> Unit,
    onDelete: (Article) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        Text(
            modifier = Modifier.padding(horizontal = MediumPadding1),
            text = "Bookmark",
            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
//            color = MaterialTheme.customColorsPalette.textTitle
        )

//        Spacer(modifier = Modifier.height(16.dp))

        ArticlesListBookmark(
            articles = state.articles,
            onClickCard = navigateToDetails,
            onDelete = onDelete
        )
    }
}