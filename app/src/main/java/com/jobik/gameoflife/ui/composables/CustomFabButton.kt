package com.jobik.gameoflife.ui.composables

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R

@Composable
fun CustomFabButton(enabled: Boolean, onClick: () -> Unit) {
    val fabHeight = 80.dp
    val fabWidth = if (enabled) fabHeight + fabHeight / 3 else fabHeight
    val radiusValue = if (enabled) fabHeight / 4 else fabHeight / 2f
    val radius by animateDpAsState(targetValue = radiusValue, label = "radius")
    val width by animateDpAsState(targetValue = fabWidth, label = "width")


    LargeFloatingActionButton(
        modifier = Modifier
            .height(fabHeight)
            .width(width),
        onClick = onClick,
        shape = RoundedCornerShape(radius)
    ) {
        Icon(
            if (enabled) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = stringResource(id = R.string.switch_running_simulation)
        )
    }
}