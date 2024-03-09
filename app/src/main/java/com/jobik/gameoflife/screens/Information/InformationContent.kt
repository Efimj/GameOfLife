package com.jobik.gameoflife.screens.Information

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R
import com.jobik.gameoflife.ui.composables.ScreenOnePaneWrapper
import com.jobik.gameoflife.ui.helpers.BottomWindowInsetsSpacer
import com.jobik.gameoflife.ui.helpers.WindowWidthSizeClass
import com.jobik.gameoflife.ui.helpers.horizontalWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.isWidth

@Composable
fun InformationContent() {
    val uriHandler = LocalUriHandler.current

    ScreenOnePaneWrapper(
        modifier = Modifier
            .horizontalWindowInsetsPadding()
            .padding(vertical = 20.dp)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        val johnConwayUri = stringResource(id = R.string.JohnHortonConway_wiki_uri)
        LargeInformationCard(
            image = R.drawable.john_horton_conway_poster,
            title = R.string.JohnHortonConway,
            body = R.string.JohnHortonConway_card_description,
            button = CardButton(
                text = R.string.open_in_wikipedia,
                onClick = { uriHandler.openUri(johnConwayUri) }
            )
        )
        val gameOfLifeUri = stringResource(id = R.string.GameOfLife_wiki_uri)
        LargeInformationCard(
            image = R.drawable.game_of_life_poster,
            title = R.string.the_game_of_life,
            body = R.string.GameOfLife_large_description,
            button = CardButton(
                text = R.string.open_in_wikipedia,
                onClick = { uriHandler.openUri(gameOfLifeUri) }
            )
        )

        val telegramGroupUri = stringResource(id = R.string.telegram_group_url)
        val githubRepositoryUri = stringResource(id = R.string.github_repository_url)

        if (isWidth(WindowWidthSizeClass.Compact)) {
            SmallInformationCard(
                image = R.drawable.telegram_icon,
                title = R.string.telegram_community,
                body = R.string.telegram_community_description
            ) {
                uriHandler.openUri(telegramGroupUri)
            }
            SmallInformationCard(
                image = R.drawable.github_icon,
                title = R.string.open_source_project,
                body = R.string.open_source_project_description
            ) {
                uriHandler.openUri(githubRepositoryUri)
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    SmallInformationCard(
                        image = R.drawable.telegram_icon,
                        title = R.string.telegram_community,
                        body = R.string.telegram_community_description
                    ) {
                        uriHandler.openUri(telegramGroupUri)
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    SmallInformationCard(
                        image = R.drawable.github_icon,
                        title = R.string.open_source_project,
                        body = R.string.open_source_project_description
                    ) {
                        uriHandler.openUri(githubRepositoryUri)
                    }
                }

            }
        }
        BottomWindowInsetsSpacer()
    }
}