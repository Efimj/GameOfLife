package com.jobik.gameoflife

import com.jobik.gameoflife.services.donation.DonationPromptService
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DonationPromptServiceTest {
    @Test
    fun promptUsesItsOwnLaunchIntervalAndDoesNotOverlapRatePrompt() {
        assertFalse(DonationPromptService.shouldPromptAtLaunch(0, true))
        assertFalse(DonationPromptService.shouldPromptAtLaunch(11, true))
        assertTrue(DonationPromptService.shouldPromptAtLaunch(12, true))
        assertTrue(DonationPromptService.shouldPromptAtLaunch(24, true))
        assertFalse(DonationPromptService.shouldPromptAtLaunch(36, true))
        assertTrue(DonationPromptService.shouldPromptAtLaunch(48, true))
    }

    @Test
    fun disabledPromptIsNeverScheduled() {
        assertFalse(DonationPromptService.shouldPromptAtLaunch(12, false))
        assertFalse(DonationPromptService.shouldPromptAtLaunch(24, false))
    }

    @Test
    fun subscriptionAlwaysPreventsPrompt() {
        assertFalse(
            DonationPromptService.canShowAfterSubscriptionCheck(
                isLoading = false,
                isSubscribed = true,
                hasBillingError = false,
            )
        )
    }

    @Test
    fun promptRequiresSuccessfulCompletedSubscriptionCheck() {
        assertFalse(
            DonationPromptService.canShowAfterSubscriptionCheck(
                isLoading = true,
                isSubscribed = false,
                hasBillingError = false,
            )
        )
        assertFalse(
            DonationPromptService.canShowAfterSubscriptionCheck(
                isLoading = false,
                isSubscribed = false,
                hasBillingError = true,
            )
        )
        assertTrue(
            DonationPromptService.canShowAfterSubscriptionCheck(
                isLoading = false,
                isSubscribed = false,
                hasBillingError = false,
            )
        )
    }
}
