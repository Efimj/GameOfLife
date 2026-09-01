package com.jobik.gameoflife.billing

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class BillingPurchaseSummary(
    val donationCount: Int = 0,
    val hasActiveSubscription: Boolean = false,
    val activeSubscriptionBasePlanId: String? = null,
) {
    val hasAnyPurchase: Boolean
        get() = donationCount > 0 || hasActiveSubscription
}

object BillingPurchaseStore {
    private val mutableSummary = MutableStateFlow(BillingPurchaseSummary())
    val summary: StateFlow<BillingPurchaseSummary> = mutableSummary.asStateFlow()

    private var preferences: SharedPreferences? = null

    @Synchronized
    fun initialize(context: Context) {
        if (preferences != null) return
        val prefs = context.applicationContext.getSharedPreferences(
            DONATIONS_PREFERENCES,
            Context.MODE_PRIVATE,
        )
        preferences = prefs
        val hasActiveSubscription = prefs.getBoolean(ACTIVE_SUBSCRIPTION_KEY, false)
        val activeBasePlanId = prefs.getString(ACTIVE_BASE_PLAN_KEY, null)
            ?: BillingCatalog.STANDARD_MONTHLY_BASE_PLAN_ID.takeIf {
                hasActiveSubscription
            }
        if (hasActiveSubscription && !prefs.contains(ACTIVE_BASE_PLAN_KEY)) {
            prefs.edit().putString(ACTIVE_BASE_PLAN_KEY, activeBasePlanId).apply()
        }
        mutableSummary.value = BillingPurchaseSummary(
            donationCount = prefs.getInt(DONATION_COUNT_KEY, 0),
            hasActiveSubscription = hasActiveSubscription,
            activeSubscriptionBasePlanId = activeBasePlanId,
        )
    }

    fun recordDonation(quantity: Int): BillingPurchaseSummary {
        val prefs = requireNotNull(preferences)
        val updated = mutableSummary.value.copy(
            donationCount = mutableSummary.value.donationCount + quantity,
        )
        prefs.edit().putInt(DONATION_COUNT_KEY, updated.donationCount).apply()
        mutableSummary.value = updated
        return updated
    }

    fun updateSubscription(
        isActive: Boolean,
        basePlanId: String? = null,
    ): BillingPurchaseSummary {
        val prefs = requireNotNull(preferences)
        val activeBasePlanId = if (isActive) {
            basePlanId
                ?: mutableSummary.value.activeSubscriptionBasePlanId
                ?: BillingCatalog.STANDARD_MONTHLY_BASE_PLAN_ID
        } else {
            null
        }
        val updated = mutableSummary.value.copy(
            hasActiveSubscription = isActive,
            activeSubscriptionBasePlanId = activeBasePlanId,
        )
        prefs.edit()
            .putBoolean(ACTIVE_SUBSCRIPTION_KEY, isActive)
            .putString(ACTIVE_BASE_PLAN_KEY, activeBasePlanId)
            .apply()
        mutableSummary.value = updated
        return updated
    }

    private const val DONATIONS_PREFERENCES = "donations_billing"
    private const val DONATION_COUNT_KEY = "donation_purchase_count"
    private const val ACTIVE_SUBSCRIPTION_KEY = "active_subscription"
    private const val ACTIVE_BASE_PLAN_KEY = "active_subscription_base_plan"
}
