package com.jobik.gameoflife.billing

import android.app.Activity
import kotlinx.coroutines.flow.StateFlow

interface DonationsBillingRepository : AutoCloseable {
    val state: StateFlow<DonationsBillingState>

    fun start()

    fun refresh()

    fun purchaseSubscription(activity: Activity, basePlanId: String)

    fun purchaseDonation(activity: Activity)

    fun clearMessage()
}
