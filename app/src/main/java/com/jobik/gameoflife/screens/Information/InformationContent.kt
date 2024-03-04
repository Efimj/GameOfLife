package com.jobik.gameoflife.screens.Information

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding

@Composable
fun InformationContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .horizontalWindowInsetsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp, top = 20.dp)
            .padding(horizontal = 20.dp),
    ) {
        JohnConwayCard()
    }
}

@Composable
private fun JohnConwayCard() {
    val uriHandler = LocalUriHandler.current
    var isExpanded by rememberSaveable() { mutableStateOf(false) }

    val containerColorValue =
        if (isExpanded) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh
    val containerColorState = animateColorAsState(targetValue = containerColorValue, label = "contentColorState")

    val contentColorValue =
        if (isExpanded) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    val contentColorState = animateColorAsState(targetValue = contentColorValue, label = "contentColorState")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable {
                isExpanded = isExpanded.not()
            },
        colors = CardDefaults.cardColors(
            containerColor = containerColorState.value,
            contentColor = contentColorState.value
        )
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large),
            painter = painterResource(id = R.drawable.john_horton_conway_poster),
            contentScale = ContentScale.FillWidth,
            contentDescription = null
        )
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.JohnHortonConway),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColorState.value
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.End
                ) {
                    val rotatingValue = if (isExpanded) 180f else 0f
                    val rotatingState = animateFloatAsState(targetValue = rotatingValue, label = "rotatingState")
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
            Text(
                modifier = Modifier.animateContentSize(),
                text = stringResource(id = R.string.JohnHortonConway_card_description),
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                color = contentColorState.value.copy(alpha = .8f)
            )
            Row {
                val uri = stringResource(id = R.string.JohnHortonConway_wiki_uri)
                Button(onClick = {
                    uriHandler.openUri(uri)
                }) {
                    Text(
                        text = stringResource(id = R.string.open_in_wikipedia),
                        maxLines = 1,
                    )
                }
            }
        }
    }
}