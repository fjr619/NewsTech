package com.fjr619.newsloc.presentation.navgraph

import com.ramcosta.composedestinations.annotation.NavGraph
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@NavGraph
annotation class NewsNavGraph(
      val start: Boolean = false
)

@NewsNavGraph(start = true)
@NavGraph
annotation class HomeNavGraph(
      val start: Boolean = false
)

@NewsNavGraph
@NavGraph
annotation class SearchNavGraph(
      val start: Boolean = false
)