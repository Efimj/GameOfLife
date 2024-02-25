package com.jobik.gameoflife.screens.Onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.jobik.gameoflife.R
import com.jobik.gameoflife.navigation.Screen
import com.jobik.gameoflife.ui.composables.VerticalIndicator
import com.jobik.gameoflife.ui.helpers.bottomWindowInsetsPadding
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreenContainer(navController: NavHostController) {
    val pagerState = rememberPagerState {
        OnboardingScreen.PageList.Count
    }

    Box(modifier = Modifier.fillMaxSize()) {
        IndicatorContent(pagerState)
        VerticalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            userScrollEnabled = true,
            horizontalAlignment = Alignment.CenterHorizontally,
            reverseLayout = false,
            contentPadding = PaddingValues(0.dp),
            beyondBoundsPageCount = 0,
            pageSize = PageSize.Fill,
        ) {
            PagerScreen(OnboardingScreen.PageList.PageList[it])
        }
        NavigationContent(pagerState = pagerState, navController = navController)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.NavigationContent(pagerState: PagerState, navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.align(Alignment.BottomCenter), contentAlignment = Alignment.BottomCenter) {
        Row(
            modifier = Modifier
                .bottomWindowInsetsPadding()
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(modifier = Modifier
                .weight(1f)
                .height(50.dp)
                .zIndex(3f),
                onClick = {
                    navController.navigate(Screen.Game.name) {
                        popUpTo(Screen.Game.name)
                    }
                }) {
                Text(text = "Start game")
            }
            AnimatedVisibility(
                visible = pagerState.currentPage < OnboardingScreen.PageList.Count - 1,
                enter = slideInHorizontally() + expandHorizontally(clip = false) + fadeIn(),
                exit = slideOutHorizontally() + shrinkHorizontally(clip = false) + fadeOut(),
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 10.dp)
                ) {
                    OutlinedButton(
                        modifier = Modifier.height(50.dp),
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
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BoxScope.IndicatorContent(pagerState: PagerState) {
    Box(modifier = Modifier.align(Alignment.TopStart), contentAlignment = Alignment.CenterStart) {
        Column(
            modifier = Modifier
                .topWindowInsetsPadding()
                .padding(top = 20.dp)
                .width(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalIndicator(pagerState)
        }
    }
}

@Composable
fun PagerScreen(content: OnboardingScreen) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 70.dp)
            .topWindowInsetsPadding()
            .bottomWindowInsetsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(modifier = Modifier.height(440.dp), contentAlignment = Alignment.Center) {
            Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxHeight(),
                painter = painterResource(id = content.image),
                contentScale = ContentScale.FillHeight,
                contentDescription = stringResource(R.string.Onboarding)
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            text = stringResource(content.title),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(top = 10.dp),
            text = stringResource(content.description),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            minLines = 4,
            maxLines = 4,
            overflow = TextOverflow.Ellipsis
        )
    }
}