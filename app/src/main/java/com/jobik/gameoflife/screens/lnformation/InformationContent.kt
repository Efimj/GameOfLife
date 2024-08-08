package com.jobik.gameoflife.screens.lnformation

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R
import com.jobik.gameoflife.services.rate.RateService
import com.jobik.gameoflife.ui.composables.modifier.fadingEdges
import com.jobik.gameoflife.ui.helpers.BottomWindowInsetsSpacer
import com.jobik.gameoflife.ui.helpers.WindowWidthSizeClass
import com.jobik.gameoflife.ui.helpers.currentWidthSizeClass

data class CardInfo(
    val id: Int,
    val image: Int,
    val title: Int,
    val body: Int,
    val imageWithTint: Boolean = false,
    @StringRes val buttonText: Int,
    val onClick: () -> Unit,
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun InformationContent() {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val currentWidthSize = currentWidthSizeClass()
    val countColumns = when (currentWidthSize) {
        WindowWidthSizeClass.Compact -> 1

        WindowWidthSizeClass.Medium -> 2

        WindowWidthSizeClass.Expanded -> 3
    }

    var selectedCard by remember { mutableStateOf<CardInfo?>(null) }

    val cards = listOf(
        CardInfo(
            id = 0,
            image = R.drawable.john_horton_conway_poster,
            title = R.string.JohnHortonConway,
            body = R.string.JohnHortonConway_card_description,
            buttonText = R.string.open_in_wikipedia,
            onClick = {
                uriHandler.openUri(context.getString(R.string.JohnHortonConway_wiki_uri))
            }
        ),
        CardInfo(
            id = 1,
            image = R.drawable.game_of_life_poster,
            title = R.string.the_game_of_life,
            body = R.string.GameOfLife_large_description,
            imageWithTint = true,
            buttonText = R.string.open_in_wikipedia,
            onClick = {
                uriHandler.openUri(context.getString(R.string.GameOfLife_wiki_uri))
            }
        ),
        CardInfo(
            id = 2,
            image = R.drawable.three_stars_v2,
            title = R.string.rate_dialog_title,
            body = R.string.rate_dialog_description,
            imageWithTint = true,
            buttonText = R.string.rate,
            onClick = {
                RateService(context).openAppRating()
            }
        )
    )

    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(countColumns),
            contentPadding = PaddingValues(10.dp),
            verticalItemSpacing = 10.dp,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = cards) { cardInfo ->
                AnimatedVisibility(
                    visible = cardInfo.id != selectedCard?.id,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut() + scaleOut(),
                ) {
                    Box(
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "${cardInfo.id}-bounds"),
                                animatedVisibilityScope = this,
                            )
                    ) {
                        LargeInformationCard(
                            modifier = Modifier
                                .sharedElement(
                                    state = rememberSharedContentState(key = cardInfo.id),
                                    animatedVisibilityScope = this@AnimatedVisibility
                                ),
                            image = cardInfo.image,
                            title = cardInfo.title,
                            body = cardInfo.body,
                            imageWithTint = cardInfo.imageWithTint,
                            onClick = {
                                selectedCard = cardInfo
                            }
                        )
                    }
                }
            }
            item {
                val telegramGroupUri = stringResource(id = R.string.telegram_group_url)

                SmallInformationCard(
                    image = R.drawable.telegram_icon,
                    title = R.string.telegram_community,
                    body = R.string.telegram_community_description
                ) {
                    uriHandler.openUri(telegramGroupUri)
                }
            }
            item {
                val githubRepositoryUri =
                    stringResource(id = R.string.github_repository_url)

                SmallInformationCard(
                    image = R.drawable.github_icon,
                    title = R.string.open_source_project,
                    body = R.string.open_source_project_description
                ) {
                    uriHandler.openUri(githubRepositoryUri)
                }
            }
            item { BottomWindowInsetsSpacer() }
        }
        CardDetails(
            card = selectedCard,
            onConfirm = {
                selectedCard = null
            }
        )
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.CardDetails(
    modifier: Modifier = Modifier,
    card: CardInfo?,
    onConfirm: () -> Unit
) {
    AnimatedContent(
        modifier = modifier,
        targetState = card,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "CardDetails"
    ) { targetCard ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (targetCard != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f))
                        .clickable { onConfirm() },
                )
                Box(
                    modifier = Modifier
                        .padding(20.dp)
                        .widthIn(max = 400.dp)
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = "${targetCard.id}-bounds"),
                            animatedVisibilityScope = this@AnimatedContent,
                        )
                ) {
                    ExpandedCard(
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState(key = targetCard.id),
                            animatedVisibilityScope = this@AnimatedContent
                        ),
                        card = targetCard,
                        onClose = onConfirm
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ExpandedCard(
    modifier: Modifier = Modifier,
    card: CardInfo,
    onClose: () -> Unit
) {
    val containerColorValue = MaterialTheme.colorScheme.surfaceContainerHigh
    val contentColorValue = MaterialTheme.colorScheme.onSurface
    val scroll = rememberScrollState()

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = containerColorValue,
        contentColor = contentColorValue
    ) {
        Column(
            modifier = Modifier
                .fadingEdges(scroll, isVertical = true)
                .verticalScroll(scroll)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large),
                painter = painterResource(id = card.image),
                contentScale = ContentScale.FillWidth,
                contentDescription = null,
                colorFilter = if (card.imageWithTint) ColorFilter.tint(MaterialTheme.colorScheme.primary) else null
            )
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .skipToLookaheadSize(),
            ) {
                Text(
                    text = stringResource(id = card.title),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColorValue
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = card.body),
                    style = MaterialTheme.typography.bodyLarge,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onClose) {
                        Text(text = stringResource(id = R.string.close))
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(onClick = card.onClick) {
                        Text(text = stringResource(id = card.buttonText))
                    }
                }
            }
        }
    }
}