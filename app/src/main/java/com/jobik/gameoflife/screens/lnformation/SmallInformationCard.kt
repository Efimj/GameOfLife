package com.jobik.gameoflife.screens.lnformation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SmallInformationCard(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,
    @StringRes title: Int,
    @StringRes body: Int,
    onClick: () -> Unit
) {
    var isExpanded by rememberSaveable() { mutableStateOf(false) }

    val containerColorValue =
        if (isExpanded) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh
    val containerColorState =
        animateColorAsState(targetValue = containerColorValue, label = "contentColorState")

    val contentColorValue =
        if (isExpanded) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    val contentColorState =
        animateColorAsState(targetValue = contentColorValue, label = "contentColorState")

    val elevationValue = if (isExpanded) 6.dp else 0.dp
    val elevationState = animateDpAsState(targetValue = elevationValue, label = "elevationState")

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        shadowElevation = elevationState.value,
        color = containerColorState.value,
        contentColor = contentColorState.value
    ) {
        Column(
            modifier = Modifier
                .clickable {
                    isExpanded = isExpanded.not()
                    onClick()
                }
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.weight(1f, true),
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColorState.value,
                    overflow = TextOverflow.Ellipsis
                )
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End
                ) {
                    Image(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = image),
                        contentScale = ContentScale.FillWidth,
                        contentDescription = null
                    )
                }
            }
            Text(
                text = stringResource(id = body),
                style = MaterialTheme.typography.bodyLarge,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
