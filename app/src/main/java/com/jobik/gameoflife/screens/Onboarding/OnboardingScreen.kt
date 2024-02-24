package com.jobik.gameoflife.screens.Onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jobik.gameoflife.ui.composables.VerticalIndicator
import com.jobik.gameoflife.ui.helpers.topWindowInsetsPadding

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen() {
    val pagerState = rememberPagerState {
        5
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.CenterStart) {
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
        VerticalPager(state = pagerState, userScrollEnabled = true) {
            when (it) {
                0 -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .topWindowInsetsPadding()
                ) {
                    Text(text = "1")
                }

                1 -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .topWindowInsetsPadding()
                ) {
                    Text(text = "2")
                }

                2 -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                        .topWindowInsetsPadding()
                ) {
                    Text(text = "3")
                }
            }
        }
    }
}