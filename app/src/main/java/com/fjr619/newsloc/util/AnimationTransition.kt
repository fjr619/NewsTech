package com.fjr619.newsloc.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavBackStackEntry

object AnimationTransition {

    val fadeIn: (Int) -> (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) =
        { duration -> { fadeIn(tween(duration)) } }

    val fadeOut: (Int) -> (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) =
        { duration -> { fadeOut(tween(duration)) } }

    val slideInLeft: (Int) -> (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) =
        { duration ->
            {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(duration)
                )
            }
        }

    val slideInRight: (Int) -> (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition?) =
        { duration ->
            {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(duration)
                )
            }
        }

    val slideOutLeft: (Int) -> (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) =
        { duration ->
            {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(duration)
                )
            }
        }

    val slideOutRight: (Int) -> (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition?) =
        { duration ->
            {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(duration)
                )
            }
        }
}