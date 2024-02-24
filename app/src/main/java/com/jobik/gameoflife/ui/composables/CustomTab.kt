package com.jobik.gameoflife.ui.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
private fun CustomTabIndicator(
    indicatorWidth: Dp,
    indicatorOffset: Dp,
    indicatorColor: Color,
) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(
                width = indicatorWidth,
            )
            .offset(
                x = indicatorOffset,
            )
            .clip(
                shape = CircleShape,
            )
            .background(
                color = indicatorColor,
            ),
    )
}

@Composable
private fun CustomTabItem(
    isSelected: Boolean,
    onClick: () -> Unit,
    activeTextColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    tabWidth: Dp,
    text: String,
) {
    val tabTextColor: Color by animateColorAsState(
        targetValue = if (isSelected) {
            activeTextColor
        } else {
            textColor
        },
        animationSpec = tween(easing = LinearEasing), label = "tabTextColor",
    )
    Text(
        modifier = Modifier
            .clip(CircleShape)
            .clickable {
                onClick()
            }
            .width(tabWidth)
            .padding(
                vertical = 8.dp,
                horizontal = 12.dp,
            ),
        text = text,
        color = tabTextColor,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.bodyMedium
    )
}

@Composable
fun CustomTab(
    selectedItemIndex: Int,
    items: List<String>,
    modifier: Modifier = Modifier,
    tabWidth: Dp = 100.dp,
    indicatorColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    onClick: (index: Int) -> Unit,
) {
    val indicatorOffset: Dp by animateDpAsState(
        targetValue = tabWidth * selectedItemIndex,
        animationSpec = tween(easing = LinearEasing), label = "indicatorOffset",
    )

    Box(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .clip(CircleShape)
            .background(Color.Transparent)
            .height(intrinsicSize = IntrinsicSize.Min)
    ) {
        CustomTabIndicator(
            indicatorWidth = tabWidth,
            indicatorOffset = indicatorOffset,
            indicatorColor = indicatorColor,
        )
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.clip(CircleShape),
        ) {
            items.mapIndexed { index, text ->
                val isSelected = index == selectedItemIndex
                CustomTabItem(
                    isSelected = isSelected,
                    onClick = {
                        onClick(index)
                    },
                    tabWidth = tabWidth,
                    text = text,
                )
            }
        }
    }
}