package com.jobik.gameoflife.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GooglePlayDonationsBillingRepository(context: Context) :
    DonationsBillingRepository,
    PurchasesUpdatedListener {

    private val purchaseStore = BillingPurchaseStore.apply { initialize(context) }
    private val initialPurchaseSummary = purchaseStore.summary.value
    private val mutableState = MutableStateFlow(
        DonationsBillingState(
            donation = DonationProduct(
                purchaseCount = initialPurchaseSummary.donationCount,
            ),
            isSubscribed = initialPurchaseSummary.hasActiveSubscription,
            activeSubscriptionBasePlanId =
                initialPurchaseSummary.activeSubscriptionBasePlanId,
        )
    )
    override val state: StateFlow<DonationsBillingState> = mutableState.asStateFlow()

    private val billingClient = BillingClient.newBuilder(context.applicationContext)
        .setListener(this)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .enableAutoServiceReconnection()
        .build()

    private var hasStarted = false
    private var pendingCatalogQueries = 0
    private var pendingCatalogError: BillingMessage? = null
    private var subscriptionDetails: ProductDetails? = null
    private var donationDetails: ProductDetails? = null
    private var subscriptionOffers = emptyMap<String, ProductDetails.SubscriptionOfferDetails>()
    private var donationOffer: ProductDetails.OneTimePurchaseOfferDetails? = null
    private var pendingSubscriptionBasePlanId: String? = null
    private val purchasesBeingConsumed = mutableSetOf<String>()

    override fun start() {
        if (hasStarted) return
        hasStarted = true
        connect()
    }

    private fun connect() {
        mutableState.update { it.copy(isLoading = true, loadError = null) }
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    mutableState.update { it.copy(isConnected = true) }
                    refresh()
                } else {
                    showServiceError()
                }
            }

            override fun onBillingServiceDisconnected() {
                mutableState.update { it.copy(isConnected = false) }
            }
        })
    }

    override fun refresh() {
        if (!hasStarted) {
            start()
            return
        }
        mutableState.update { it.copy(isLoading = true, loadError = null) }
        queryCatalog()
        queryOwnedPurchases()
    }

    private fun queryCatalog() {
        pendingCatalogQueries = 4
        pendingCatalogError = null
        queryProduct(
            productId = BillingCatalog.SUBSCRIPTION_PRODUCT_ID,
            productType = BillingClient.ProductType.SUBS,
        ) { details ->
            subscriptionDetails = details
            subscriptionOffers = details?.subscriptionOfferDetails
                .orEmpty()
                .groupBy { it.basePlanId }
                .mapValues { (_, offers) ->
                    offers.firstOrNull { it.offerId == null } ?: offers.first()
                }

            mutableState.update { current ->
                current.copy(
                    subscriptionPlans = current.subscriptionPlans.map { plan ->
                        val offer = subscriptionOffers[plan.basePlanId]
                        plan.copy(
                            localizedName = details?.name,
                            localizedPrice = offer?.pricingPhases?.pricingPhaseList
                                ?.lastOrNull()?.formattedPrice,
                            isAvailable = offer != null,
                        )
                    }
                )
            }
        }

        queryProduct(
            productId = BillingCatalog.DONATION_PRODUCT_ID,
            productType = BillingClient.ProductType.INAPP,
        ) { details ->
            donationDetails = details
            donationOffer = details?.oneTimePurchaseOfferDetailsList
                ?.firstOrNull {
                    it.purchaseOptionId == BillingCatalog.DONATION_PURCHASE_OPTION_ID
                }
                ?: details?.oneTimePurchaseOfferDetails?.takeIf {
                    it.purchaseOptionId == BillingCatalog.DONATION_PURCHASE_OPTION_ID
                }
            mutableState.update {
                it.copy(
                    donation = it.donation.copy(
                        localizedPrice = donationOffer?.formattedPrice,
                        isAvailable = donationOffer != null,
                    )
                )
            }
        }
    }

    private fun queryProduct(
        productId: String,
        productType: String,
        onResult: (ProductDetails?) -> Unit,
    ) {
        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(productId)
            .setProductType(productType)
            .build()
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()

        billingClient.queryProductDetailsAsync(params) { result, queryResult ->
            val details = if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                mutableState.update { it.copy(isConnected = true) }
                queryResult.productDetailsList.firstOrNull()
            } else {
                pendingCatalogError = BillingMessage.SERVICE_UNAVAILABLE
                null
            }
            onResult(details)
            finishLoadingQuery()
        }
    }

    private fun queryOwnedPurchases() {
        queryPurchases(BillingClient.ProductType.SUBS) { purchases ->
            val active = purchases.any { purchase ->
                purchase.purchaseState == Purchase.PurchaseState.PURCHASED &&
                    BillingCatalog.SUBSCRIPTION_PRODUCT_ID in purchase.products
            }
            val purchaseSummary = purchaseStore.updateSubscription(isActive = active)
            mutableState.update {
                it.copy(
                    isSubscribed = active,
                    activeSubscriptionBasePlanId =
                        purchaseSummary.activeSubscriptionBasePlanId,
                )
            }
            processPurchases(purchases)
        }
        queryPurchases(BillingClient.ProductType.INAPP, ::processPurchases)
    }

    private fun queryPurchases(productType: String, onResult: (List<Purchase>) -> Unit) {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(productType)
            .build()
        billingClient.queryPurchasesAsync(params) { result, purchases ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                onResult(purchases)
            } else {
                pendingCatalogError = BillingMessage.SERVICE_UNAVAILABLE
            }
            finishLoadingQuery()
        }
    }

    private fun finishLoadingQuery() {
        pendingCatalogQueries -= 1
        if (pendingCatalogQueries != 0) return

        mutableState.update { current ->
            val hasAllProducts = current.subscriptionPlans.all { it.isAvailable } &&
                current.donation.isAvailable
            current.copy(
                isLoading = false,
                loadError = pendingCatalogError
                    ?: if (hasAllProducts) null
                    else BillingMessage.PRODUCT_UNAVAILABLE,
            )
        }
    }

    override fun purchaseSubscription(activity: Activity, basePlanId: String) {
        val details = subscriptionDetails
        val offer = subscriptionOffers[basePlanId]
        if (details == null || offer == null) {
            mutableState.update { it.copy(message = BillingMessage.PRODUCT_UNAVAILABLE) }
            return
        }
        pendingSubscriptionBasePlanId = basePlanId
        if (!launchBillingFlow(activity, details, offer.offerToken)) {
            pendingSubscriptionBasePlanId = null
        }
    }

    override fun purchaseDonation(activity: Activity) {
        val details = donationDetails
        val offer = donationOffer
        if (details == null || offer == null) {
            mutableState.update { it.copy(message = BillingMessage.PRODUCT_UNAVAILABLE) }
            return
        }
        launchBillingFlow(activity, details, offer.offerToken)
    }

    private fun launchBillingFlow(
        activity: Activity,
        details: ProductDetails,
        offerToken: String?,
    ): Boolean {
        val productParams = BillingFlowParams.ProductDetailsParams.newBuilder()
            .setProductDetails(details)
            .apply {
                if (!offerToken.isNullOrBlank()) setOfferToken(offerToken)
            }
            .build()
        val result = billingClient.launchBillingFlow(
            activity,
            BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(listOf(productParams))
                .build()
        )
        if (result.responseCode != BillingClient.BillingResponseCode.OK) {
            mutableState.update { it.copy(message = BillingMessage.SERVICE_UNAVAILABLE) }
        }
        return result.responseCode == BillingClient.BillingResponseCode.OK
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: List<Purchase>?) {
        when (result.responseCode) {
            BillingClient.BillingResponseCode.OK -> processPurchases(purchases.orEmpty())
            BillingClient.BillingResponseCode.USER_CANCELED -> Unit
            else -> mutableState.update {
                it.copy(message = BillingMessage.SERVICE_UNAVAILABLE)
            }
        }
    }

    private fun processPurchases(purchases: List<Purchase>) {
        purchases.forEach { purchase ->
            when (purchase.purchaseState) {
                Purchase.PurchaseState.PENDING -> mutableState.update {
                    it.copy(message = BillingMessage.PURCHASE_PENDING)
                }

                Purchase.PurchaseState.PURCHASED -> when {
                    BillingCatalog.SUBSCRIPTION_PRODUCT_ID in purchase.products -> {
                        val purchaseSummary = purchaseStore.updateSubscription(
                            isActive = true,
                            basePlanId = pendingSubscriptionBasePlanId,
                        )
                        pendingSubscriptionBasePlanId = null
                        mutableState.update {
                            it.copy(
                                isSubscribed = true,
                                activeSubscriptionBasePlanId =
                                    purchaseSummary.activeSubscriptionBasePlanId,
                            )
                        }
                        acknowledgeSubscription(purchase)
                    }

                    BillingCatalog.DONATION_PRODUCT_ID in purchase.products -> {
                        consumeDonation(purchase)
                    }
                }
            }
        }
    }

    private fun acknowledgeSubscription(purchase: Purchase) {
        if (purchase.isAcknowledged) return
        val params = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient.acknowledgePurchase(params) { result ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                mutableState.update { it.copy(message = BillingMessage.PURCHASE_COMPLETE) }
            }
        }
    }

    private fun consumeDonation(purchase: Purchase) {
        if (!purchasesBeingConsumed.add(purchase.purchaseToken)) return
        val params = ConsumeParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClient.consumeAsync(params) { result, token ->
            purchasesBeingConsumed.remove(token)
            if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                val purchaseSummary = purchaseStore.recordDonation(purchase.quantity)
                mutableState.update {
                    it.copy(
                        donation = it.donation.copy(
                            purchaseCount = purchaseSummary.donationCount
                        ),
                        message = BillingMessage.PURCHASE_COMPLETE,
                    )
                }
            }
        }
    }

    private fun showServiceError() {
        mutableState.update {
            it.copy(
                isLoading = false,
                isConnected = false,
                loadError = BillingMessage.SERVICE_UNAVAILABLE,
            )
        }
    }

    override fun clearMessage() {
        mutableState.update { it.copy(message = null) }
    }

    override fun close() {
        billingClient.endConnection()
        hasStarted = false
    }

}
