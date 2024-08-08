package com.jobik.gameoflife.screens.lnformation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun LargeInformationCard(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,
    @StringRes title: Int,
    @StringRes body: Int,
    imageWithTint: Boolean = false,
    onClick: () -> Unit
) {
    val containerColorValue = MaterialTheme.colorScheme.surfaceContainerHigh
    val contentColorValue = MaterialTheme.colorScheme.onSurface

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = containerColorValue,
        contentColor = contentColorValue
    ) {
        Column(modifier = Modifier
            .clickable { onClick() }) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large),
                painter = painterResource(id = image),
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
                colorFilter = if (imageWithTint) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null
            )
            Column(
                modifier = Modifier.padding(20.dp),
            ) {
                Text(
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColorValue
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = body),
                    modifier = Modifier.animateContentSize(),
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 3,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}