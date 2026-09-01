package com.jobik.gameoflife.billing

enum class SubscriptionTier {
    STANDARD,
    GENEROUS,
}

data class SubscriptionPlan(
    val tier: SubscriptionTier,
    val basePlanId: String,
    val localizedName: String? = null,
    val localizedPrice: String? = null,
    val isAvailable: Boolean = false,
)

data class DonationProduct(
    val localizedPrice: String? = null,
    val isAvailable: Boolean = false,
    val purchaseCount: Int = 0,
)

enum class BillingMessage {
    PURCHASE_COMPLETE,
    PURCHASE_PENDING,
    SERVICE_UNAVAILABLE,
    PRODUCT_UNAVAILABLE,
}

data class DonationsBillingState(
    val isLoading: Boolean = true,
    val isConnected: Boolean = false,
    val isSubscribed: Boolean = false,
    val activeSubscriptionBasePlanId: String? = null,
    val subscriptionPlans: List<SubscriptionPlan> = listOf(
        SubscriptionPlan(
            tier = SubscriptionTier.STANDARD,
            basePlanId = BillingCatalog.STANDARD_MONTHLY_BASE_PLAN_ID,
        ),
        SubscriptionPlan(
            tier = SubscriptionTier.GENEROUS,
            basePlanId = BillingCatalog.GENEROUS_MONTHLY_BASE_PLAN_ID,
        ),
    ),
    val donation: DonationProduct = DonationProduct(),
    val loadError: BillingMessage? = null,
    val message: BillingMessage? = null,
)
