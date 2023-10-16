package com.fjr619.newsloc.presentation.detail

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fjr619.newsloc.R
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.Dimens
import com.fjr619.newsloc.presentation.detail.components.DetailTopBar
import com.fjr619.newsloc.ui.theme.customColorsPalette
import com.fjr619.newsloc.util.UiEffect

@Composable
fun DetailContent(
      article: Article,
      navigateUp: () -> Unit
) {
      val context = LocalContext.current

//      val factory = remember {
//            derivedStateOf {
//                  EntryPointAccessors.fromActivity(
//                        context as Activity, MainActivity.ViewModelFactoryProvider::class.java
//                  ).detailViewModelFactory()
//            }
//
//      }
//
//      val viewModel: DetailViewModel = viewModel(
//            factory = DetailViewModel.provideFactory(factory.value, article)
//

      val viewModel: DetailViewModel = hiltViewModel()

      val sideEffect by viewModel.sideEffect.collectAsStateWithLifecycle(
            initialValue = UiEffect.None()
      )

      LaunchedEffect(key1 = sideEffect) {

            sideEffect.apply {
                  when(this){
                        is UiEffect.Toast ->{
                              Toast.makeText(context, message.asString(context), Toast.LENGTH_SHORT).show()
                        }
                        else -> Unit
                  }
            }
      }

      Scaffold(
            modifier = Modifier
                  .fillMaxSize()
                  .statusBarsPadding(),
            topBar = {
                  DetailTopBar(
                        bookmarkArticle = viewModel.bookmarkArticle.value,
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
                              viewModel.onEvent(DetailEvent.UpsertDeleteArticle(article))
                        },
                        onBackClick = navigateUp
                  )
            }
      ) {paddingValues ->
            LazyColumn(
                  modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                              top = paddingValues.calculateTopPadding(),
                              bottom = paddingValues.calculateBottomPadding(),
                        ),
                  contentPadding = PaddingValues(
                        start = Dimens.MediumPadding1,
                        end = Dimens.MediumPadding1,
//                top = MediumPadding1
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
                                    .fillMaxWidth()
                                    .height(Dimens.ArticleImageHeight)
                                    .clip(MaterialTheme.shapes.medium),
                              contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(Dimens.MediumPadding1))
                        Text(
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