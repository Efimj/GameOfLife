package com.jobik.gameoflife.services.donation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jobik.gameoflife.billing.GooglePlayDonationsBillingRepository
import com.jobik.gameoflife.navigation.Donations
import com.jobik.gameoflife.navigation.NavigationHelper.Companion.navigateToTopLevel
import com.jobik.gameoflife.services.app.AppCounter

@Composable
fun DonationDialogProvider(navController: NavHostController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val applicationContext = context.applicationContext
    val promptService = remember(applicationContext) {
        DonationPromptService(applicationContext)
    }
    val launchCount = remember(applicationContext) {
        AppCounter(applicationContext).getOnCreateNumber()
    }
    val shouldCheckSubscription = remember(promptService, launchCount) {
        promptService.shouldCheckSubscription(launchCount)
    }

    if (!shouldCheckSubscription) return

    val repository = remember(applicationContext) {
        GooglePlayDonationsBillingRepository(applicationContext)
    }
    val billingState by repository.state.collectAsStateWithLifecycle()
    val isOpen = rememberSaveable { mutableStateOf(false) }
    val subscriptionCheckHandled = rememberSaveable { mutableStateOf(false) }

    DisposableEffect(repository) {
        repository.start()
        onDispose(repository::close)
    }

    LaunchedEffect(
        billingState.isLoading,
        billingState.isSubscribed,
        billingState.loadError,
    ) {
        if (subscriptionCheckHandled.value || billingState.isLoading) return@LaunchedEffect

        subscriptionCheckHandled.value = true
        isOpen.value = DonationPromptService.canShowAfterSubscriptionCheck(
            isLoading = billingState.isLoading,
            isSubscribed = billingState.isSubscribed,
            hasBillingError = billingState.loadError != null,
        )
    }

    fun rememberChoice(dontAskAgain: Boolean) {
        if (dontAskAgain) promptService.updateCanAskForDonation(false)
    }

    DonationPromptDialog(
        isOpen = isOpen,
        onCancel = { dontAskAgain ->
            rememberChoice(dontAskAgain)
            isOpen.value = false
        },
        onDonate = { dontAskAgain ->
            rememberChoice(dontAskAgain)
            isOpen.value = false
            navController.navigateToTopLevel(Donations)
        },
    )
}
