package com.jobik.gameoflife.ui.composables.modifier

import androidx.compose.foundation.basicMarquee
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.constrainWidth
import androidx.compose.ui.unit.dp
import com.gigamole.composefadingedges.fill.FadingEdgesFillType
import com.gigamole.composefadingedges.marqueeHorizontalFadingEdges

fun Modifier.marquee(
    edgeColor: Color = Color.Unspecified,
) = this.composed {
    var showMarquee by remember { mutableStateOf(false) }

    Modifier
        .clipToBounds()
        .then(
            if (showMarquee) Modifier.marqueeHorizontalFadingEdges(
                fillType = if (edgeColor.isSpecified) {
                    FadingEdgesFillType.FadeColor(
                        color = edgeColor
                    )
                } else FadingEdgesFillType.FadeClip(),
                length = 10.dp,
                isMarqueeAutoLayout = false
            ) { Modifier.basicMarquee(Int.MAX_VALUE, velocity = 30.dp) }
            else Modifier
        )
        .layout { measurable, constraints ->
            val childConstraints = constraints.copy(maxWidth = Constraints.Infinity)
            val placeable = measurable.measure(childConstraints)
            val containerWidth = constraints.constrainWidth(placeable.width)
            val contentWidth = placeable.width
            if (!showMarquee) {
                showMarquee = contentWidth > containerWidth
            }
            layout(containerWidth, placeable.height) {
                placeable.placeWithLayer(x = 0, y = 0)
            }
        }
}