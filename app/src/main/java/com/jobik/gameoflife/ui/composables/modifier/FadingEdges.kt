package com.jobik.gameoflife.ui.composables.modifier

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gigamole.composefadingedges.FadingEdgesGravity
import com.gigamole.composefadingedges.content.FadingEdgesContentType
import com.gigamole.composefadingedges.content.scrollconfig.FadingEdgesScrollConfig
import com.gigamole.composefadingedges.fill.FadingEdgesFillType
import com.gigamole.composefadingedges.horizontalFadingEdges
import com.gigamole.composefadingedges.verticalFadingEdges


@SuppressLint("UnnecessaryComposedModifier")
fun Modifier.fadingEdges(
    scrollableState: ScrollableState? = null,
    color: Color = Color.Unspecified,
    isVertical: Boolean = false,
    spanCount: Int? = null,
    scrollFactor: Float = 1.25f,
    enabled: Boolean = true,
    length: Dp = 16.dp,
    gravity: FadingEdgesGravity = FadingEdgesGravity.All
) = this.composed {
    if (!enabled) Modifier
    else {
        val fillType = if (color.isSpecified) {
            FadingEdgesFillType.FadeColor(
                color = color
            )
        } else {
            FadingEdgesFillType.FadeClip()
        }

        val scrollConfig = FadingEdgesScrollConfig.Dynamic(
            scrollFactor = scrollFactor
        )

        when (scrollableState) {
            is ScrollState -> {
                if (isVertical) {
                    Modifier.verticalFadingEdges(
                        gravity = gravity,
                        contentType = FadingEdgesContentType.Dynamic.Scroll(
                            scrollConfig = scrollConfig,
                            state = scrollableState
                        ),
                        fillType = fillType,
                        length = length
                    )
                } else {
                    Modifier.horizontalFadingEdges(
                        gravity = gravity,
                        contentType = FadingEdgesContentType.Dynamic.Scroll(
                            scrollConfig = scrollConfig,
                            state = scrollableState
                        ),
                        fillType = fillType,
                        length = length
                    )
                }
            }

            is LazyListState -> {
                if (isVertical) {
                    Modifier.verticalFadingEdges(
                        gravity = gravity,
                        contentType = FadingEdgesContentType.Dynamic.Lazy.List(
                            scrollConfig = scrollConfig,
                            state = scrollableState
                        ),
                        fillType = fillType,
                        length = length
                    )
                } else {
                    Modifier.horizontalFadingEdges(
                        gravity = gravity,
                        contentType = FadingEdgesContentType.Dynamic.Lazy.List(
                            scrollConfig = scrollConfig,
                            state = scrollableState
                        ),
                        fillType = fillType,
                        length = length
                    )
                }
            }

            is LazyGridState -> {
                require(spanCount != null)
                if (isVertical) {
                    Modifier.verticalFadingEdges(
                        gravity = gravity,
                        contentType = FadingEdgesContentType.Dynamic.Lazy.Grid(
                            scrollConfig = scrollConfig,
                            state = scrollableState,
                            spanCount = spanCount
                        ),
                        fillType = fillType,
                        length = length
                    )
                } else {
                    Modifier.horizontalFadingEdges(
                        gravity = gravity,
                        contentType = FadingEdgesContentType.Dynamic.Lazy.Grid(
                            scrollConfig = scrollConfig,
                            state = scrollableState,
                            spanCount = spanCount
                        ),
                        fillType = fillType,
                        length = length
                    )
                }
            }

            is LazyStaggeredGridState -> {
                require(spanCount != null)
                if (isVertical) {
                    Modifier.verticalFadingEdges(
                        gravity = gravity,
                        contentType = FadingEdgesContentType.Dynamic.Lazy.StaggeredGrid(
                            scrollConfig = scrollConfig,
                            state = scrollableState,
                            spanCount = spanCount
                        ),
                        fillType = fillType,
                        length = length
                    )
                } else {
                    Modifier.horizontalFadingEdges(
                        gravity = gravity,
                        contentType = FadingEdgesContentType.Dynamic.Lazy.StaggeredGrid(
                            scrollConfig = scrollConfig,
                            state = scrollableState,
                            spanCount = spanCount
                        ),
                        fillType = fillType,
                        length = length
                    )
                }
            }

            else -> {
                if (isVertical) {
                    Modifier.verticalFadingEdges(
                        gravity = gravity,
                        contentType = FadingEdgesContentType.Static,
                        fillType = fillType,
                        length = length
                    )
                } else {
                    Modifier.horizontalFadingEdges(
                        gravity = gravity,
                        contentType = FadingEdgesContentType.Static,
                        fillType = fillType,
                        length = length
                    )
                }
            }
        }
    }
}