package com.jobik.gameoflife.screens.game

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.R
import com.jobik.gameoflife.gameOfLife.GameOfLife
import com.jobik.gameoflife.screens.game.actions.GameActions
import com.jobik.gameoflife.ui.composables.modifier.fadingEdges
import com.jobik.gameoflife.ui.helpers.WindowWidthSizeClass
import com.jobik.gameoflife.ui.helpers.currentWidthSizeClass
import com.jobik.gameoflife.ui.helpers.isWidth
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding
import com.jobik.gameoflife.util.settings.SettingsManager
import com.jobik.gameoflife.util.settings.SettingsManager.settings
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.SnapConfig
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.PartySystem
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Rotation
import nl.dionsegijn.konfetti.core.Spread
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit

@Composable
fun GameScreen(
    viewModel: GameScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current

    BackHandler(enabled = viewModel.states.value.isSimulationRunning) {
        viewModel.turnOffSimulation()
    }

    when (currentWidthSizeClass()) {
        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> {
            CompactGameScreen(viewModel)
        }

        WindowWidthSizeClass.Expanded -> {
            ExpandedGameScreen(viewModel)
        }
    }

    LaunchedEffect(viewModel.states.value.gameSettings) {
        SettingsManager.update(
            context = context,
            settings = settings.copy(gameSettings = viewModel.states.value.gameSettings)
        )
    }

    Confetti(viewModel = viewModel)
}

@Composable
fun Confetti(viewModel: GameScreenViewModel) {
    val state = viewModel.states.value

    var partyView by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(state.gameResult) {
        partyView = when (state.gameResult) {
            GameOfLife.Companion.GameOfLifeResult.Loop -> true
            GameOfLife.Companion.GameOfLifeResult.StableCombination -> true
            else -> false
        }
    }

    fun rain(
    ): List<Party> = Party(
        speed = 10f,
        maxSpeed = 30f,
        damping = 0.9f,
        emitter = Emitter(duration = 3, TimeUnit.SECONDS).perSecond(100),
    ).let { party ->
        listOf(
            party.copy(
                angle = 45,
                position = Position.Relative(0.0, 0.0),
                spread = 90,
            ),
            party.copy(
                angle = 90,
                position = Position.Relative(0.5, 0.0),
                spread = 360,
            ),
            party.copy(
                angle = 135,
                position = Position.Relative(1.0, 0.0),
                spread = 90,
            )
        )
    }

    if (partyView) {
        KonfettiView(
            modifier = Modifier.fillMaxSize(),
            parties = rain(),
            updateListener = PartyListener(onFinish = { partyView = false })
        )
    }
}

class PartyListener(val onFinish: () -> Unit) : OnParticleSystemUpdateListener {
    override fun onParticleSystemEnded(system: PartySystem, activeSystems: Int) {
        onFinish()
    }
}

@Composable
private fun ExpandedGameScreen(
    viewModel: GameScreenViewModel,
) {
    val localDensity = LocalDensity.current
    val context = LocalContext.current

    val containerColorTarget = when {
        viewModel.states.value.isSimulationRunning -> MaterialTheme.colorScheme.secondary
        viewModel.states.value.gameResult == GameOfLife.Companion.GameOfLifeResult.NoOneSurvived -> MaterialTheme.colorScheme.error
        viewModel.states.value.gameResult != null -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceContainerHighest
    }
    val containerColor by animateColorAsState(
        targetValue = containerColorTarget,
        label = "containerColor"
    )

    val contentColorTarget = when {
        viewModel.states.value.isSimulationRunning -> MaterialTheme.colorScheme.onSecondary
        viewModel.states.value.gameResult == GameOfLife.Companion.GameOfLifeResult.NoOneSurvived -> MaterialTheme.colorScheme.onError
        viewModel.states.value.gameResult != null -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSurface
    }
    val contentColor by animateColorAsState(
        targetValue = contentColorTarget,
        label = "contentColor"
    )

    var wideScreenHeight by remember { mutableStateOf(0.dp) }

    Row(
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                // Set screen height using the LayoutCoordinates
                wideScreenHeight = with(localDensity) { coordinates.size.height.toDp() }
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        val scroll = rememberScrollState()
        Column(
            modifier = Modifier
                .weight(.4f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .fadingEdges(scroll, isVertical = true)
                .verticalScroll(scroll)
        ) {
            GameActions(viewModel = viewModel)
        }
        Column(
            modifier = Modifier
                .weight(.6f)
                .clip(RoundedCornerShape(12.dp))
                .fillMaxHeight()
                .background(containerColor),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedContent(
                    targetState = getTitleText(
                        context = context,
                        result = viewModel.states.value.gameResult
                    ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.headlineSmall,
                        color = contentColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            GameContent(viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CompactGameScreen(
    viewModel: GameScreenViewModel
) {
    val containerColorTarget = when {
        viewModel.states.value.isSimulationRunning -> MaterialTheme.colorScheme.secondary
        viewModel.states.value.gameResult == GameOfLife.Companion.GameOfLifeResult.NoOneSurvived -> MaterialTheme.colorScheme.error
        viewModel.states.value.gameResult != null -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surfaceContainerHighest
    }
    val containerColor by animateColorAsState(
        targetValue = containerColorTarget,
        label = "containerColor"
    )

    val contentColorTarget = when {
        viewModel.states.value.isSimulationRunning -> MaterialTheme.colorScheme.onSecondary
        viewModel.states.value.gameResult == GameOfLife.Companion.GameOfLifeResult.NoOneSurvived -> MaterialTheme.colorScheme.onError
        viewModel.states.value.gameResult != null -> MaterialTheme.colorScheme.onPrimary
        else -> MaterialTheme.colorScheme.onSurface
    }
    val contentColor by animateColorAsState(
        targetValue = contentColorTarget,
        label = "contentColor"
    )

    val context = LocalContext.current

    var pinned by rememberSaveable { mutableStateOf(false) }

    val collapsingToolbarScaffold = rememberCollapsingToolbarScaffoldState()

    val isCompact = isWidth(sizeClass = WindowWidthSizeClass.Compact)

    val topInsets = if (isCompact) {
        Modifier.topWindowInsetsPadding()
    } else {
        Modifier
    }

    CollapsingToolbarScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(containerColorTarget)
            .then(topInsets),
        state = collapsingToolbarScaffold,
        scrollStrategy = ScrollStrategy.EnterAlways,
        enabledWhenBodyUnfilled = false,
        snapConfig = SnapConfig(), // "collapseThreshold = 0.5" by default
        toolbar = {
            GameAppBar(
                modifier = Modifier.background(containerColorTarget),
                title = getTitleText(context = context, result = viewModel.states.value.gameResult),
                color = contentColor,
                backgroundColor = containerColor,
                isPinned = pinned,
                onPin = {
                    pinned = !pinned
                }
            )
        },
    ) {
        LazyColumn(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
        ) {
            if (pinned) {
                stickyHeader {
                    GameContent(
                        modifier = Modifier
                            .clip(
                                MaterialTheme.shapes.large.copy(
                                    topStart = ZeroCornerSize,
                                    topEnd = ZeroCornerSize
                                )
                            )
                            .background(containerColor),
                        viewModel = viewModel
                    )
                }
            } else {
                item {
                    GameContent(
                        modifier = Modifier
                            .clip(
                                MaterialTheme.shapes.large.copy(
                                    topStart = ZeroCornerSize,
                                    topEnd = ZeroCornerSize
                                )
                            )
                            .background(containerColor),
                        viewModel = viewModel
                    )
                }
            }
            item {
                GameActions(viewModel = viewModel)
            }
        }
    }
}

private fun getTitleText(
    context: Context,
    result: GameOfLife.Companion.GameOfLifeResult?
): String {
    return when (result) {
        GameOfLife.Companion.GameOfLifeResult.StableCombination -> context.getString(R.string.stable_combination)

        GameOfLife.Companion.GameOfLifeResult.NoOneSurvived -> context.getString(R.string.no_one_survived)

        GameOfLife.Companion.GameOfLifeResult.Loop -> context.getString(R.string.loop_combination)

        null -> context.getString(R.string.GameOfLife)
    }
}
