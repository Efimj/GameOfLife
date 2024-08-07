package com.jobik.gameoflife.screens.onboarding

import android.content.Context
import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.jobik.gameoflife.R
import com.jobik.gameoflife.SharedPreferencesKeys
import com.jobik.gameoflife.SharedPreferencesKeys.OnboardingFinishedData
import com.jobik.gameoflife.navigation.NavigationHelper.Companion.canNavigate
import com.jobik.gameoflife.navigation.Screen
import com.jobik.gameoflife.ui.composables.VerticalIndicator
import com.jobik.gameoflife.ui.helpers.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navController: NavHostController) {
    val pagerState = rememberPagerState {
        Onboarding.PageList.Count
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .horizontalWindowInsetsPadding()
    ) {
        IndicatorContent(pagerState)
        VerticalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            userScrollEnabled = true,
            horizontalAlignment = Alignment.CenterHorizontally,
            reverseLayout = false,
            contentPadding = PaddingValues(0.dp),
            pageSize = PageSize.Fill,
        ) {
            PagerScreen(Onboarding.PageList.PageList[it])
        }
        NavigationContent(pagerState = pagerState, navController = navController)
    }
}

fun onFinished(context: Context, navController: NavHostController) {
    try {
        val sharedPreferences =
            context.getSharedPreferences(SharedPreferencesKeys.AppSettings, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(SharedPreferencesKeys.CurrentOnboardingFinishedData, OnboardingFinishedData)
            .apply()
    } catch (e: Exception) {
        Log.i("onboarding - onFinished", e.toString())
    }
    if (navController.canNavigate().not()) return
    navController.navigate(Screen.Game) {
        popUpTo(Screen.Game)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.NavigationContent(pagerState: PagerState, navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val bottomPaddings = if (WindowWidthSizeClass.Compact.name == currentWidthSizeClass().name) {
        Modifier.bottomWindowInsetsPadding()
    } else {
        Modifier
    }

    Box(modifier = Modifier.align(Alignment.BottomCenter), contentAlignment = Alignment.BottomCenter) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(bottomPaddings)
                .padding(horizontal = 40.dp)
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (currentWidthSizeClass().name == WindowWidthSizeClass.Compact.name) {
                Button(modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
                    .zIndex(3f),
                    onClick = {
                        onFinished(context = context, navController = navController)
                    }) {
                    Text(text = stringResource(id = R.string.start_game))
                }
                AnimatedVisibility(
                    visible = pagerState.currentPage < Onboarding.PageList.Count - 1,
                    enter = slideInHorizontally() + expandHorizontally(clip = false) + fadeIn(),
                    exit = slideOutHorizontally() + shrinkHorizontally(clip = false) + fadeOut(),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(start = 10.dp)
                    ) {
                        OutlinedButton(
                            modifier = Modifier
                                .height(50.dp),
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(
                                        pagerState.currentPage + 1, animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioNoBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                }
                            }) {
                            Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = "")
                        }
                    }
                }
            } else {
                val nextButtonWeight = if (pagerState.currentPage < Onboarding.PageList.Count - 1) 1f else .3f
                val nextButtonWeightState =
                    animateFloatAsState(targetValue = nextButtonWeight, label = "startGameButtonWeightState")

                OutlinedButton(
                    modifier = Modifier
                        .weight(nextButtonWeightState.value)
                        .padding(end = 10.dp)
                        .height(50.dp),
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(
                                pagerState.currentPage + 1, animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioNoBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        }
                    }) {
                    Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = "")
                }
                Button(modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                    onClick = {
                        onFinished(context = context, navController = navController)
                    }) {
                    Text(text = stringResource(id = R.string.start_game))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.IndicatorContent(pagerState: PagerState) {
    val topPaddings = if (WindowWidthSizeClass.Compact.name == currentWidthSizeClass().name) {
        Modifier.topWindowInsetsPadding()
    } else {
        Modifier
    }

    Box(modifier = Modifier.align(Alignment.TopStart), contentAlignment = Alignment.CenterStart) {
        Column(
            modifier = Modifier
                .then(topPaddings)
                .padding(top = 20.dp)
                .width(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalIndicator(pagerState)
        }
    }
}

@Composable
fun PagerScreen(content: Onboarding) {
    val verticalPaddings = if (WindowWidthSizeClass.Compact.name == currentWidthSizeClass().name) {
        Modifier.verticalWindowInsetsPadding()
    } else {
        Modifier

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(verticalPaddings)
            .padding(bottom = 90.dp)
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        var index by remember { mutableIntStateOf(0) }
        val currentDrawable = remember { mutableIntStateOf(content.images[index]) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(1000)
                index = (index + 1) % content.images.size
                currentDrawable.intValue = content.images[index]
            }
        }
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Crossfade(
                targetState = currentDrawable.intValue,
                animationSpec = tween(durationMillis = 500),
                label = "crossfade"
            ) { targetDrawable ->
                Image(
                    alignment = Alignment.Center,
                    painter = painterResource(id = targetDrawable),
                    contentScale = ContentScale.Fit,
                    contentDescription = stringResource(R.string.Onboarding),
                    colorFilter = if (content.images.size == 1) null else ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(content.title),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            text = stringResource(content.description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis
        )
    }
}