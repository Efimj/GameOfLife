package com.jobik.gameoflife.screens.donations

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jobik.gameoflife.billing.DonationsBillingRepository
import com.jobik.gameoflife.billing.GooglePlayDonationsBillingRepository

class DonationsViewModel(
    private val billingRepository: DonationsBillingRepository,
) : ViewModel() {
    val state = billingRepository.state

    init {
        billingRepository.start()
    }

    fun refresh() = billingRepository.refresh()

    fun purchaseSubscription(activity: Activity, basePlanId: String) {
        billingRepository.purchaseSubscription(activity, basePlanId)
    }

    fun purchaseDonation(activity: Activity) {
        billingRepository.purchaseDonation(activity)
    }

    fun clearMessage() = billingRepository.clearMessage()

    override fun onCleared() {
        billingRepository.close()
        super.onCleared()
    }

    class Factory(context: Context) : ViewModelProvider.Factory {
        private val applicationContext = context.applicationContext

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass.isAssignableFrom(DonationsViewModel::class.java))
            return DonationsViewModel(
                GooglePlayDonationsBillingRepository(applicationContext)
            ) as T
        }
    }
}
