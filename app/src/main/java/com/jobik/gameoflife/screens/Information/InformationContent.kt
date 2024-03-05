package com.jobik.gameoflife.screens.Information

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .horizontalWindowInsetsPadding()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp, top = 20.dp)
            .padding(horizontal = 20.dp),
    ) {
        val johnConwayUri = stringResource(id = R.string.JohnHortonConway_wiki_uri)
        InformationCard(
            image = R.drawable.john_horton_conway_poster,
            title = R.string.JohnHortonConway,
            body = R.string.JohnHortonConway_card_description,
            button = CardButton(
                text = R.string.open_in_wikipedia,
                onClick = { uriHandler.openUri(johnConwayUri) }
            )
        )
        val gameOfLifeUri = stringResource(id = R.string.GameOfLife_wiki_uri)
        InformationCard(
            image = R.drawable.john_horton_conway_poster,
            title = R.string.JohnHortonConway,
            body = R.string.JohnHortonConway_card_description,
            button = CardButton(
                text = R.string.open_in_wikipedia,
                onClick = { uriHandler.openUri(gameOfLifeUri) }
            )
        )
    }
}