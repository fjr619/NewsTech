package com.fjr619.newsloc.presentation.onboarding.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.fjr619.newsloc.R
import com.fjr619.newsloc.presentation.Dimens
import com.fjr619.newsloc.presentation.onboarding.Page
import com.fjr619.newsloc.ui.theme.NewsLOCTheme

@Composable
fun OnboardingPage(
    modifier: Modifier = Modifier,
    page: Page
) {
    Column(modifier = modifier) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.60f),
            painter = painterResource(id = page.image),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(Dimens.MediumPadding1))
        Text(
            modifier = Modifier.padding(horizontal = Dimens.MediumPadding2),
            text = page.title,
            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
        )
        Text(
            modifier = Modifier.padding(horizontal = Dimens.MediumPadding2),
            text = page.description,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun OnBoardingPagePreview() {
    NewsLOCTheme {
        OnboardingPage(
            page = Page(
                title = "Lorem Ipsum is simply dummy",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry.",
                image = R.drawable.onboarding1
            )
        )
    }
}