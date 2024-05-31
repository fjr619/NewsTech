package com.fjr619.newsloc.presentation.detail.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.window.core.layout.WindowWidthSizeClass
import com.fjr619.newsloc.R
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.ui.theme.NewsLOCTheme
import com.fjr619.newsloc.ui.theme.customColorsPalette

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailTopBar(
    bookmarkArticle: Article? = null,
    onBrowsingClick: () -> Unit,
    onShareClick: () -> Unit,
    onBookMarkClick: () -> Unit,
    onBackClick: () -> Unit,
) {

    val adaptiveInfo = currentWindowAdaptiveInfo()

    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            actionIconContentColor = MaterialTheme.customColorsPalette.body,
            navigationIconContentColor = MaterialTheme.customColorsPalette.body,
        ),
        title = {},
        navigationIcon = {
            if (adaptiveInfo.windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.EXPANDED) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back_arrow),
                        contentDescription = null,
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onBookMarkClick) {
                Icon(painter = painterResource(if (bookmarkArticle == null) R.drawable.ic_bookmark else R.drawable.ic_bookmar_filled), contentDescription = null)
            }
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = null
                )
            }
            IconButton(onClick = onBrowsingClick) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_network),
                    contentDescription = null
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun DetailsTopBarPreview() {
    NewsLOCTheme(dynamicColor = false) {
        DetailTopBar(
            onShareClick = { /*TODO*/ },
            onBookMarkClick = { /*TODO*/ },
            onBrowsingClick = {},) {
        }
    }
}