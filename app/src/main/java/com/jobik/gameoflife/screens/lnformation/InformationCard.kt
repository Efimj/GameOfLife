package com.jobik.gameoflife.screens.lnformation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

data class CardButton(
    @StringRes val text: Int,
    val onClick: () -> Unit
)

@Composable
fun LargeInformationCard(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,
    @StringRes title: Int,
    @StringRes body: Int,
    imageWithTint: Boolean = false,
    button: CardButton,
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
        Column(modifier = Modifier
            .clickable {
                isExpanded = isExpanded.not()
            }) {
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(id = title),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = contentColorState.value
                    )
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.End
                    ) {
                        val rotatingValue = if (isExpanded) 180f else 0f
                        val rotatingState = animateFloatAsState(
                            targetValue = rotatingValue,
                            label = "rotatingState"
                        )
                        IconButton(
                            modifier = Modifier
                                .size(30.dp)
                                .rotate(rotatingState.value),
                            onClick = { isExpanded = isExpanded.not() }) {
                            Icon(
                                imageVector = Icons.Outlined.ExpandMore,
                                contentDescription = null,
                                tint = contentColorState.value.copy(alpha = .8f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = body),
                    modifier = Modifier.animateContentSize(),
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                AnimatedVisibility(visible = isExpanded) {
                    Column {
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(onClick = button.onClick) {
                            Text(
                                text = stringResource(id = button.text),
                                maxLines = 1,
                            )
                        }
                    }
                }
            }
        }
    }
}