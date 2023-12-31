package com.fjr619.newsloc.presentation.bookmark

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.Dimens
import com.fjr619.newsloc.presentation.Dimens.MediumPadding1
import com.fjr619.newsloc.presentation.common.ArticlesList2
import com.fjr619.newsloc.ui.theme.customColorsPalette

@Composable
fun BookmarkScreen(
    paddingValues: PaddingValues,
    state: BookmarkState,
    navigateToDetails: (Article) -> Unit
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

        ArticlesList2(
            articles = state.articles,
            onClickCard = navigateToDetails
        )
    }
}