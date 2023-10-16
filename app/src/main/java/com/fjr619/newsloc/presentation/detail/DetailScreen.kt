package com.fjr619.newsloc.presentation.detail

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import com.fjr619.newsloc.domain.model.Article
import com.fjr619.newsloc.presentation.navgraph.HomeNavGraph
import com.fjr619.newsloc.presentation.navgraph.SearchNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@HomeNavGraph
@Destination
@Composable
fun DetailScreen(
      article: Article,
      destination: DestinationsNavigator
) {
      DetailContent(article = article, navigateUp = {
            destination.navigateUp()
      })
}

@SearchNavGraph
@Destination
@Composable
fun DetailScreenFromSearch(
      article: Article,
      destination: DestinationsNavigator
) {
      DetailContent(article = article, navigateUp = {
            destination.navigateUp()
      })
}