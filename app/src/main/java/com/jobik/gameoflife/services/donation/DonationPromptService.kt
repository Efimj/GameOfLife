package com.jobik.gameoflife.services.donation

import android.content.Context
import com.jobik.gameoflife.SharedPreferencesKeys

class DonationPromptService(private val context: Context) {
    fun canAskForDonation(): Boolean {
        val preferences = context.getSharedPreferences(
            SharedPreferencesKeys.AppSettings,
            Context.MODE_PRIVATE,
        )
        return preferences.getBoolean(SharedPreferencesKeys.CanAskDonation, true)
    }

    fun updateCanAskForDonation(canAsk: Boolean) {
        val preferences = context.getSharedPreferences(
            SharedPreferencesKeys.AppSettings,
            Context.MODE_PRIVATE,
        )
        preferences.edit()
            .putBoolean(SharedPreferencesKeys.CanAskDonation, canAsk)
            .apply()
    }

    fun shouldCheckSubscription(launchCount: Int): Boolean =
        shouldPromptAtLaunch(
            launchCount = launchCount,
            canAskForDonation = canAskForDonation(),
        )

    companion object {
        private const val DonationPromptInterval = 12
        private const val RatePromptInterval = 9

        internal fun shouldPromptAtLaunch(
            launchCount: Int,
            canAskForDonation: Boolean,
        ): Boolean = canAskForDonation &&
            launchCount >= DonationPromptInterval &&
            launchCount % DonationPromptInterval == 0 &&
            launchCount % RatePromptInterval != 0

        internal fun canShowAfterSubscriptionCheck(
            isLoading: Boolean,
            isSubscribed: Boolean,
            hasBillingError: Boolean,
        ): Boolean = !isLoading && !isSubscribed && !hasBillingError
    }
}
