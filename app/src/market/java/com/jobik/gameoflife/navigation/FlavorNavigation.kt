package com.jobik.gameoflife.navigation

import androidx.annotation.Keep
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jobik.gameoflife.screens.donations.DonationsScreen
import kotlinx.serialization.Serializable

@Keep
@Serializable
data object Donations : Screen(name = "Donations")

fun NavGraphBuilder.addFlavorDestinations() {
    composable<Donations>(
        enterTransition = { NavigationTransition().mainScreenEnterTransition(this) },
        exitTransition = { NavigationTransition().mainScreenExitTransition(this) },
    ) {
        DonationsScreen()
    }
}

fun flavorScreens(): List<Screen> = listOf(Donations)
