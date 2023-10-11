package com.fjr619.newsloc.presentation.news_navigator.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp

/**
 * https://blog.devgenius.io/animated-bottom-navigation-in-jetpack-compose-af8f590fbeca
 */
@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun RowScope.AnimatedBottomNavigationItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    label: String,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    selectedContentColor: Color = LocalContentColor.current,
) {
    val top by animateDpAsState(
        targetValue = if (selected) 0.dp else 80.dp,
        animationSpec = SpringSpec(dampingRatio = 0.5f, stiffness = 200f), label = ""
    )

    Box(
        modifier = modifier
//            .height(80.dp)
            .padding(start = 10.dp, end = 10.dp)
            .weight(1f)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onClick.invoke()
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            tint = selectedContentColor,
            contentDescription = null,
            modifier = Modifier
                .height(80.dp)
                .width(26.dp)
                .offset(y = top)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(80.dp)
                .offset(y = top - 80.dp)
        ) {
            Text(
                text = label,
            )
        }
    }
}