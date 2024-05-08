package com.fjr619.newsloc.presentation.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fjr619.newsloc.R
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.Dimens.ArticleImageHeight
import com.fjr619.newsloc.presentation.Dimens.MediumPadding1
import com.fjr619.newsloc.presentation.detail.components.DetailTopBar
import com.fjr619.newsloc.presentation.navgraph.LocalAnimatedVisibilityScope
import com.fjr619.newsloc.ui.theme.customColorsPalette

@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SharedTransitionScope.DetailScreen(
    prefixSharedKey: String,
    article: Article,
    bookmarkArticle: Article?,
    event: (DetailEvent) -> Unit,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current
    val animatedVisibilityScope = LocalAnimatedVisibilityScope.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            DetailTopBar(
                bookmarkArticle = bookmarkArticle,
                onBrowsingClick = {
                    Intent(Intent.ACTION_VIEW).also {
                        it.data = Uri.parse(article.url)
                        if (it.resolveActivity(context.packageManager) != null) {
                            context.startActivity(it)
                        }
                    }
                },
                onShareClick = {
                    Intent(Intent.ACTION_SEND).also {
                        it.putExtra(Intent.EXTRA_TEXT, article.url)
                        it.type = "text/plain"
                        if (it.resolveActivity(context.packageManager) != null) {
                            context.startActivity(it)
                        }
                    }
                },
                onBookMarkClick = {
                    Log.e("TAG","onBookMarkClick")
                    event(DetailEvent.UpsertDeleteArticle(article))
                },
                onBackClick = navigateUp
            )
        }
    ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            contentPadding = PaddingValues(
                start = MediumPadding1,
                end = MediumPadding1,
            )
        ) {
            item {
                AsyncImage(
                    model = ImageRequest
                        .Builder(context = context)
                        .data(article.urlToImage)
                        .error(R.drawable.ic_logo)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .sharedElement(
                            rememberSharedContentState(key = "$prefixSharedKey-image-${article.url}"),
                            animatedVisibilityScope
                        )
                        .fillMaxWidth()
                        .height(ArticleImageHeight)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(MediumPadding1))
                Text(
                    modifier = Modifier.sharedBounds(
                        rememberSharedContentState(key = "$prefixSharedKey-text-${article.url}"),
                        animatedVisibilityScope
                    ),
                    text = article.title,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.customColorsPalette.textTitle
                )
                Text(
                    text = article.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.customColorsPalette.body
                )
            }
        }
    }
}

//@OptIn(ExperimentalSharedTransitionApi::class)
//@Preview(showBackground = true)
//@Composable
//fun SharedTransitionScope.DetailsScreenPreview() {
//    NewsLOCTheme(dynamicColor = false) {
//        DetailScreen(
//            animatedVisibilityScope = this@composable,
//            article = Article(
//                author = "",
//                title = "Coinbase says Apple blocked its last app release on NFTs in Wallet ... - CryptoSaurus",
//                description = "Coinbase says Apple blocked its last app release on NFTs in Wallet ... - CryptoSaurus",
//                content = "We use cookies and data to Deliver and maintain Google services Track outages and protect against spam, fraud, and abuse Measure audience engagement and site statistics to unde… [+1131 chars]",
//                publishedAt = "2023-06-16T22:24:33Z",
//                source = Source(
//                    id = "", name = "bbc"
//                ),
//                url = "https://consent.google.com/ml?continue=https://news.google.com/rss/articles/CBMiaWh0dHBzOi8vY3J5cHRvc2F1cnVzLnRlY2gvY29pbmJhc2Utc2F5cy1hcHBsZS1ibG9ja2VkLWl0cy1sYXN0LWFwcC1yZWxlYXNlLW9uLW5mdHMtaW4td2FsbGV0LXJldXRlcnMtY29tL9IBAA?oc%3D5&gl=FR&hl=en-US&cm=2&pc=n&src=1",
//                urlToImage = "https://media.wired.com/photos/6495d5e893ba5cd8bbdc95af/191:100/w_1280,c_limit/The-EU-Rules-Phone-Batteries-Must-Be-Replaceable-Gear-2BE6PRN.jpg"
//            ),
//            event = {},
//            bookmarkArticle = null
//        ) {
//
//        }
//    }
//}